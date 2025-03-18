package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaDetalharDTO;
import br.com.nedson.Controle_Orcamento.service.ReceitaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ReceitaControllerTest {

    @MockitoBean
    private ReceitaService receitaService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ReceitaCadastrarDTO> jsonCadastrar;

    @Autowired
    private JacksonTester<ReceitaAtualizarDTO> jsonAtualizar;

    private ReceitaCadastrarDTO criarReceitaCadastrarDTO() {
        return new ReceitaCadastrarDTO("Aluguel", new BigDecimal("800.00"), "01/01/2025");
    }

    private ReceitaDetalharDTO criarReceitaDetalharDTO() {
        return new ReceitaDetalharDTO("Aluguel", new BigDecimal("800.00"), "01/01/2025");
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 - Bad Request - ao tentar cadastrar uma receita com dados nulos")
    void cadastrarDadosNulos() throws Exception{
        var dto = new ReceitaCadastrarDTO(null, null, null);

        mvc.perform(post("/receitas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(
                        Map.of("campo", "descricao", "mensagem", "A descrição é obrigatória!"),
                        Map.of("campo", "valor", "mensagem", "O valor é obrigatório!"),
                        Map.of("campo", "data", "mensagem", "A data é obrigatória!"))));
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 - Bad Request - ao tentar cadastrar uma receita com dados inválidos")
    void cadastrarDadosInvalidos() throws Exception{
        var dto = new ReceitaCadastrarDTO("", new BigDecimal("00.00"), "2015-02-01");

        mvc.perform(post("/receitas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(
                        Map.of("campo", "descricao", "mensagem", "A descrição é obrigatória!"),
                        Map.of("campo", "valor", "mensagem", "O valor deve ser maior que zero!"),
                        Map.of("campo", "data", "mensagem", "A data deve estar no formato dd/MM/yyyy!"))));
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 201 - Created - ao cadastrar uma receita com dados válidos")
    void cadastrarDadosValidos() throws Exception{
        var dto = criarReceitaCadastrarDTO();

        when(receitaService.cadastrar(dto)).thenReturn(criarReceitaDetalharDTO());

        mvc.perform(post("/receitas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao", is(dto.descricao())))
                .andExpect(jsonPath("$.valor", is(dto.valor().doubleValue())))
                .andExpect(jsonPath("$.data", is(dto.data())));

        verify(receitaService, times(1)).cadastrar(any(ReceitaCadastrarDTO.class));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"Aluguel"})
    @WithMockUser
    @DisplayName("Deveria devolver 200 - Ok e a lista de receitas - ao buscar despesas com ou sem descrição ou descrição nula")
    void listarReceitasPorDescricao(String descricao) throws Exception {
        mockListarDespesas(descricao, List.of(criarReceitaDetalharDTO()));

        mvc.perform(get("/receitas")
                        .param("descricao", descricao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())));

        if (descricao == null) {
            descricao = "";
        }
        verify(receitaService, times(1)).listarByDescricao(eq(descricao), any(Pageable.class));
    }

    private void mockListarDespesas(String descricao, List<ReceitaDetalharDTO> despesas) {
        var page = new PageImpl<>(despesas);
        if (descricao == null) {
            descricao = "";
        }
        when(receitaService.listarByDescricao(eq(descricao), any(Pageable.class))).thenReturn(page);
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 200 - Ok e a lista vazia - ao buscar receitas com descrição  na requisição")
    void listarReceitasComDescricaoNaoEncontrada() throws Exception{
        when(receitaService.listarByDescricao(eq("Aluguel"), any(Pageable.class))).thenReturn(Page.empty());

        mvc.perform(get("/receitas")
                        .param("descricao", "Aluguel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(empty())));

        verify(receitaService, times(1)).listarByDescricao(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok - e listar os detalhes da receita quando id válido e existente")
    @WithMockUser
    void listarReceitasByIdEncontrado() throws Exception {
        var id = 1L;
        var dto = criarReceitaCadastrarDTO();

        when(receitaService.listarById(id))
                .thenReturn(criarReceitaDetalharDTO());

        mvc.perform(get("/receitas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is(dto.descricao())))
                .andExpect(jsonPath("$.valor", is(dto.valor().doubleValue())))
                .andExpect(jsonPath("$.data", is(dto.data())));

        verify(receitaService, times(1)).listarById(anyLong());
    }

    @Test
    @DisplayName("Deveria devolver status 404 - Not Found - quando id não for encontrado")
    @WithMockUser
    void listarReceitasByIdNaoEncontrado() throws Exception {
        var id = 999L;

        when(receitaService.listarById(anyLong()))
                .thenThrow(new EntityNotFoundException("Receita não encontrada!"));

        mvc.perform(get("/receitas/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Receita não encontrada")));

        verify(receitaService, times(1)).listarById(anyLong());
    }

    @Test
    @DisplayName("Deveria devolver status 400 - Bad Request - quando id for de tipo inválido")
    @WithMockUser
    void listarReceitasByIdTipoInvalido() throws Exception {
        when(receitaService.listarById(anyLong()))
                .thenThrow(MethodArgumentTypeMismatchException.class);

        mvc.perform(get("/receitas/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("deve ser um número inteiro válido")));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok - quando ano e mes forem válidos")
    @WithMockUser
    void listarReceitasByAnoEMesValidos() throws Exception {
        var page = new PageImpl<>(List.of(criarReceitaDetalharDTO()));

        when(receitaService.listarByAnoAndMes(eq(2025), eq(1), any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(get("/receitas/2025/01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())));

        verify(receitaService, times(1)).listarByAnoAndMes(anyInt(), anyInt(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok e a lista vazia - quando ano e mes forem válidos e não encontrar receitas")
    @WithMockUser
    void listarReceitasByAnoEMesEDespesasNaoEncontradas() throws Exception {
        when(receitaService.listarByAnoAndMes(eq(2025), eq(1), any(Pageable.class)))
                .thenReturn(Page.empty());

        mvc.perform(get("/receitas/2025/01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(empty())));

        verify(receitaService, times(1)).listarByAnoAndMes(anyInt(), anyInt(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deveria devolver status 400 - Bad Request - quando ano e mes forem inválidos")
    @WithMockUser
    void listarReceitasByAnoOuMesInvalidos() throws Exception {
        when(receitaService.listarByAnoAndMes(anyInt(), anyInt(), any(Pageable.class)))
                .thenThrow(MethodArgumentTypeMismatchException.class);

        mvc.perform(get("/receitas/asd/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("deve ser um número inteiro válido")));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok - ao tentar atualizar receita com dados válidos")
    @WithMockUser
    void atualizarReceitasComDadosValidos() throws Exception {
        var id = 1L;
        var dtoAtualizar = new ReceitaAtualizarDTO(1L, "Novo Aluguel", new BigDecimal("500.00"), "02/01/2025");
        var dtoDetalhar = new ReceitaDetalharDTO("Novo Aluguel", new BigDecimal("500.00"), "02/01/2025");

        when(receitaService.atualizar(dtoAtualizar)).thenReturn(dtoDetalhar);

        mvc.perform(put("/receitas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dtoAtualizar).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is(dtoDetalhar.descricao())))
                .andExpect(jsonPath("$.valor", is(dtoDetalhar.valor().doubleValue())))
                .andExpect(jsonPath("$.data", is(dtoDetalhar.data())));

        verify(receitaService, times(1)).atualizar(any(ReceitaAtualizarDTO.class));
    }

    @Test
    @DisplayName("Deveria devolver status 400 - Bad Request - ao tentar atualizar receita com dados inválidos")
    @WithMockUser
    void atualizarReceitasComDadosInvalidos() throws Exception {
        var id = 1L;
        var dtoAtualizar = new ReceitaAtualizarDTO(id, "", new BigDecimal("00.00"), "2015-02-01");

        mvc.perform(put("/receitas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dtoAtualizar).getJson()))
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(
                        Map.of("campo", "valor", "mensagem", "O valor deve ser maior que zero!"),
                        Map.of("campo", "data", "mensagem", "A data deve estar no formato dd/MM/yyyy!"))));
    }

    @Test
    @DisplayName("Deveria devolver status 404 - Not Found - ao tentar atualizar receita com o id não encontrado")
    @WithMockUser
    void atualizarReceitasIdNaoEncontrado() throws Exception {
        var id = 999L;
        var dtoAtualizar = new ReceitaAtualizarDTO(id, "Nova Feira", new BigDecimal("500.00"), "02/01/2025");

        when(receitaService.atualizar(any(ReceitaAtualizarDTO.class)))
                .thenThrow(new EntityNotFoundException("Receita não encontrada!"));

        mvc.perform(put("/receitas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dtoAtualizar).getJson()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Receita não encontrada")));

        verify(receitaService, times(1)).atualizar(any(ReceitaAtualizarDTO.class));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok - ao tentar deletar uma receita com id encontrado")
    @WithMockUser
    void deletarReceitasIdEncontrado() throws Exception {
        doNothing().when(receitaService).deletar(anyLong());

        mvc.perform(delete("/receitas/1"))
                .andExpect(status().isNoContent());

        verify(receitaService, times(1)).deletar(anyLong());
    }

    @Test
    @DisplayName("Deveria devolver status 404 - Not Found - ao tentar deletar uma receita com id não encontrado")
    @WithMockUser
    void deletarReceitasIdNaoEncontrado() throws Exception {
        doThrow(new EntityNotFoundException("Receita não encontrada!"))
                .when(receitaService).deletar(anyLong());

        mvc.perform(delete("/receitas/1"))
                .andExpect(status().isNotFound());

        verify(receitaService, times(1)).deletar(anyLong());
    }

    @Test
    @DisplayName("Deveria devolver status 400 - Bad Request - ao tentar deletar uma receita com id de tipo inválido")
    @WithMockUser
    void deletarReceitasIdTipoInvalido() throws Exception {
        doThrow(MethodArgumentTypeMismatchException.class)
                .when(receitaService).deletar(anyLong());

        mvc.perform(delete("/receitas/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("deve ser um número inteiro válido")));
    }
}