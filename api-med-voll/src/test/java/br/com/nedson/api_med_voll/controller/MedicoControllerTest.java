package br.com.nedson.api_med_voll.controller;

import br.com.nedson.api_med_voll.dto.endereco.EnderecoDTO;
import br.com.nedson.api_med_voll.dto.medico.*;
import br.com.nedson.api_med_voll.model.Endereco;
import br.com.nedson.api_med_voll.model.Medico;
import br.com.nedson.api_med_voll.repository.MedicoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;




@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastrarMedico> jsonRequestCadastrar;

    @Autowired
    private JacksonTester<DadosDetalharMedico> jsonResponseDetalhar;

    @Autowired
    private JacksonTester<Page<DadosBuscarMedico>> jsonResponseBuscar;

    @Autowired
    private JacksonTester<DadosAtualizarMedico> jsonRequestAtualizar;

    @MockitoBean
    private MedicoRepository repository;

    @Test
    @DisplayName("Deveria devolver codigo 403 quando usuário não tiver logado")
    void cadastrar_cenario1() throws Exception {
        var response = mvc.perform(post("/medicos"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo 400 quando os dados forem inválidos")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
        var response = mvc.perform(post("/medicos"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo 201 quando informacoes estao válidas")
    @WithMockUser
    void cadastrar_cenario3() throws Exception {
        var dadosMedico = dadosCadastrarMedico();

        when(repository.save(any())).thenReturn(new Medico(dadosMedico));

        var response = mvc.perform(post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestCadastrar.write(dadosMedico).getJson()))
                .andReturn().getResponse();

        var dadosDetalharMedico = new DadosDetalharMedico(
                null,
                dadosMedico.nome(),
                dadosMedico.email(),
                dadosMedico.crm(),
                dadosMedico.telefone(),
                dadosMedico.especialidade(),
                new Endereco(dadosMedico.endereco())
        );
        var jsonEsperado = jsonResponseDetalhar.write(dadosDetalharMedico).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código HTTP 403 quando usuário não estiver autenticado")
    void listar_cenario1() throws Exception {
        var response = mvc.perform(get("/medicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria devolver código 200 e retornar a lista de médicos")
    @WithMockUser
    void listar_cenario2() throws Exception {
        var medico = criarMedico();
        var page = new PageImpl<>(List.of(medico));

        when(repository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(page);

        var response = mvc.perform(get("/medicos")
                        .param("size", "10").param("sort", "nome,asc"))
                .andReturn().getResponse();
        var pageEsperada = page.map(DadosBuscarMedico::new);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var objectMapper = new ObjectMapper();

        Map<String, Object> jsonEsperado = objectMapper.readValue(
                jsonResponseBuscar.write(pageEsperada).getJson(), new TypeReference<>() {});

        Map<String, Object> jsonAtual = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<>() {});

        assertThat(jsonAtual).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria listar médicos com paginação e ordenação por nome")
    @WithMockUser
    void listar_cenario3() throws Exception {
        var medico = criarMedico();
        var page = new PageImpl<>(List.of(medico));

        when(repository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(page);

        var response = mvc.perform(get("/medicos")
                        .param("page", "0").param("size", "5").param("sort", "nome,desc"))
                .andReturn().getResponse();
        var pageEsperada = page.map(DadosBuscarMedico::new);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var objectMapper = new ObjectMapper();

        Map<String, Object> jsonEsperado = objectMapper.readValue(
                jsonResponseBuscar.write(pageEsperada).getJson(), new TypeReference<>() {});

        Map<String, Object> jsonAtual = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<>() {});

        assertThat(jsonAtual).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código 403 quando o usuário não tiver logado")
    void atualizar_cenario1() throws Exception {
        var response = mvc.perform(put("/medicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria devolver código 400 quando os dados forem inválidos")
    @WithMockUser
    void atualizar_cenario2() throws Exception {
        var response = mvc.perform(put("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código 404 quando o médico não existir")
    @WithMockUser
    void atualizar_cenario3() throws Exception {
        var dadosAtualizar = new DadosAtualizarMedico(criarMedico().getId(),
                criarMedico().getNome(), criarMedico().getTelefone(), dadosEndereco());

        when(repository.getReferenceById(1L)).thenThrow(new EntityNotFoundException());

        var response = mvc.perform(put("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAtualizar.write(dadosAtualizar).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver código 200 quando os dados forem válidos e o médico existir")
    @WithMockUser
    void atualizar_cenario4() throws Exception {
        var dadosAtualizar = new DadosAtualizarMedico(1L, "Atualizado", "61889999999", dadosEndereco());
        var medico = criarMedico();

        when(repository.getReferenceById(1L)).thenReturn(medico);

        var medicoAtualizado = atualizarMedico(dadosAtualizar);
        var response = mvc.perform(put("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAtualizar.write(dadosAtualizar).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = jsonResponseDetalhar.write(new DadosDetalharMedico(medicoAtualizado)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código 403 quando o usuário não tiver logado")
    void detalhar_cenario1() throws Exception {
        var response = mvc.perform(get("/medicos/1"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria devolver código 404 quando o médico não existir")
    @WithMockUser
    void detalhar_cenario2() throws Exception {
        when(repository.getReferenceById(1L)).thenThrow(new EntityNotFoundException());

        var response = mvc.perform(get("/medicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver código 200 quando o médico existir")
    @WithMockUser
    void detalhar_cenario3() throws Exception {
        var medico = criarMedico();

        when(repository.getReferenceById(1L)).thenReturn(medico);

        var response = mvc.perform(get("/medicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = jsonResponseDetalhar.write(new DadosDetalharMedico(medico)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código 403 quando o usuário não tiver logado")
    void excluir_cenario1() throws Exception {
        var response = mvc.perform(delete("/medicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria devolver código 403 quando o usuário não for ADMIN")
    @WithMockUser(roles = "USER")
    void excluir_cenario2() throws Exception {
        var response = mvc.perform(delete("/medicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria devolver código 404 quando o médico não existir")
    @WithMockUser(roles = "ADMIN")
    void excluir_cenario3() throws Exception {
        when(repository.getReferenceById(1L)).thenThrow(new EntityNotFoundException());

        var response = mvc.perform(delete("/medicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver código 204 quando o médico for excluído com sucesso")
    @WithMockUser(roles = "ADMIN")
    void excluir_cenario4() throws Exception {
        var medico = criarMedico();

        when(repository.getReferenceById(1L)).thenReturn(medico);

        var response = mvc.perform(delete("/medicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Medico criarMedico(){
        return new Medico(1L,
                dadosCadastrarMedico().nome(),
                dadosCadastrarMedico().email(),
                dadosCadastrarMedico().telefone(),
                dadosCadastrarMedico().crm(),
                dadosCadastrarMedico().especialidade(),
                new Endereco(dadosEndereco()),
                true);
    }

    private Medico atualizarMedico(DadosAtualizarMedico dados){
        return new Medico(1L,
                dados.nome(),
                dadosCadastrarMedico().email(),
                dados.telefone(),
                dadosCadastrarMedico().crm(),
                dadosCadastrarMedico().especialidade(),
                new Endereco(dados.endereco()),
                true);
    }

    private DadosBuscarMedico dadosBuscarMedico() {
        return new DadosBuscarMedico(
                1L,
                dadosDetalharMedico().nome(),
                dadosDetalharMedico().email(),
                dadosDetalharMedico().crm(),
                dadosDetalharMedico().especialidade()
        );
    }

    private DadosCadastrarMedico dadosCadastrarMedico(){
        return new DadosCadastrarMedico(
                "Medico",
                "medico@med.voll",
                "61999999999",
                "123456",
                Especialidade.CARDIOLOGIA,
                dadosEndereco());
    }

    private DadosDetalharMedico dadosDetalharMedico(){
        return new DadosDetalharMedico(new Medico(dadosCadastrarMedico()));
    }

    private EnderecoDTO dadosEndereco() {
        return new EnderecoDTO(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}