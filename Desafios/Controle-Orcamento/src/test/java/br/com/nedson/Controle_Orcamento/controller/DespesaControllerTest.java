package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaDetalharDTO;
import br.com.nedson.Controle_Orcamento.model.Categoria;
import br.com.nedson.Controle_Orcamento.service.DespesaService;
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
class DespesaControllerTest {

    @MockitoBean
    private DespesaService despesaService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DespesaCadastrarDTO> jsonCadastrar;

    @Autowired
    private JacksonTester<DespesaAtualizarDTO> jsonAtualizar;

    private DespesaCadastrarDTO criarDespesaCadastrarDTO() {
        return new DespesaCadastrarDTO("Feira", new BigDecimal("800.00"), "01/01/2025", Categoria.ALIMENTACAO);
    }

    private DespesaDetalharDTO criarDespesaDetalharDTO() {
        return new DespesaDetalharDTO("Feira", new BigDecimal("800.00"), "01/01/2025", Categoria.ALIMENTACAO);
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 - Bad Request - ao tentar cadastrar uma despesa com dados nulos")
    void cadastrarDadosNulos() throws Exception{
        var dto = new DespesaCadastrarDTO(null, null, null, null);

        mvc.perform(post("/despesas")
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
    @DisplayName("Deveria devolver 400 - Bad Request - ao tentar cadastrar uma despesa com dados inválidos")
    void cadastrarDadosInvalidos() throws Exception{
        var dto = new DespesaCadastrarDTO("", new BigDecimal("00.00"), "2015-02-01", null);

        mvc.perform(post("/despesas")
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
    @DisplayName("Deveria devolver 201 - Created - ao cadastrar uma despesa com dados válidos")
    void cadastrarDadosValidos() throws Exception{
        var dto = criarDespesaCadastrarDTO();

        when(despesaService.cadastrar(dto)).thenReturn(criarDespesaDetalharDTO());

        mvc.perform(post("/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dto).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao", is(dto.descricao())))
                .andExpect(jsonPath("$.valor", is(dto.valor().doubleValue())))
                .andExpect(jsonPath("$.data", is(dto.data())))
                .andExpect(jsonPath("$.categoria", is(dto.categoria().toString())));

        verify(despesaService, times(1)).cadastrar(any(DespesaCadastrarDTO.class));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"Feira"})
    @WithMockUser
    @DisplayName("Deveria devolver 200 - Ok e a lista de despesas - ao buscar despesas com ou sem descrição ou descrição nula")
    void listarDespesasPorDescricao(String descricao) throws Exception {
        mockListarDespesas(descricao, List.of(criarDespesaDetalharDTO()));

        mvc.perform(get("/despesas")
                        .param("descricao", descricao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())));

        if (descricao == null) {
            descricao = "";
        }
        verify(despesaService, times(1)).listarByDescricao(eq(descricao), any(Pageable.class));
    }

    private void mockListarDespesas(String descricao, List<DespesaDetalharDTO> despesas) {
        var page = new PageImpl<>(despesas);
        if (descricao == null) {
            descricao = "";
        }
        when(despesaService.listarByDescricao(eq(descricao), any())).thenReturn(page);
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 200 - Ok e a lista vazia - ao buscar despesas com descrição  na requisição")
    void listarDespesasComDescricaoNaoEncontrada() throws Exception{
        when(despesaService.listarByDescricao(eq("Feira"), any(Pageable.class))).thenReturn(Page.empty());

        mvc.perform(get("/despesas")
                        .param("descricao", "Feira"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(empty())));

        verify(despesaService, times(1)).listarByDescricao(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok - e listar os detalhes da despesa quando id válido e existente")
    @WithMockUser
    void listarDespesasByIdEncontrado() throws Exception {
        var id = 1L;
        var dto = criarDespesaCadastrarDTO();

        when(despesaService.listarById(id))
                .thenReturn(criarDespesaDetalharDTO());

        mvc.perform(get("/despesas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is(dto.descricao())))
                .andExpect(jsonPath("$.valor", is(dto.valor().doubleValue())))
                .andExpect(jsonPath("$.data", is(dto.data())))
                .andExpect(jsonPath("$.categoria", is(dto.categoria().toString())));

        verify(despesaService, times(1)).listarById(anyLong());
    }

    @Test
    @DisplayName("Deveria devolver status 404 - Not Found - quando id não for encontrado")
    @WithMockUser
    void listarDespesasByIdNaoEncontrado() throws Exception {
        var id = 999L;

        when(despesaService.listarById(anyLong()))
                .thenThrow(new EntityNotFoundException("Despesa não encontrada!"));

        mvc.perform(get("/despesas/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Despesa não encontrada")));
    }

    @Test
    @DisplayName("Deveria devolver status 400 - Bad Request - quando id for de tipo inválido")
    @WithMockUser
    void listarDespesasByIdTipoInvalido() throws Exception {
        when(despesaService.listarById(anyLong()))
                .thenThrow(MethodArgumentTypeMismatchException.class);

        mvc.perform(get("/despesas/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("deve ser um número inteiro válido")));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok - quando ano e mes forem válidos")
    @WithMockUser
    void listarDespesasByAnoEMesValidos() throws Exception {
        var page = new PageImpl<>(List.of(criarDespesaDetalharDTO()));

        when(despesaService.listarByAnoAndMes(eq(2025), eq(1), any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(get("/despesas/2025/01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())));

        verify(despesaService, times(1)).listarByAnoAndMes(anyInt(), anyInt(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok e a lista vazia - quando ano e mes forem válidos e não encontrar despesas")
    @WithMockUser
    void listarDespesasByAnoEMesEDespesasNaoEncontradas() throws Exception {
        when(despesaService.listarByAnoAndMes(eq(2025), eq(1), any()))
                .thenReturn(Page.empty());

        mvc.perform(get("/despesas/2025/01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(empty())));

        verify(despesaService, times(1)).listarByAnoAndMes(anyInt(), anyInt(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deveria devolver status 400 - Bad Request - quando ano e mes forem inválidos")
    @WithMockUser
    void listarDespesasByAnoOuMesInvalidos() throws Exception {
        when(despesaService.listarByAnoAndMes(anyInt(), anyInt(), any(Pageable.class)))
                .thenThrow(MethodArgumentTypeMismatchException.class);

        mvc.perform(get("/despesas/asd/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("deve ser um número inteiro válido")));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok - ao tentar atualizar despesa com dados válidos")
    @WithMockUser
    void atualizarDespesasComDadosValidos() throws Exception {
        var dtoAtualizar = new DespesaAtualizarDTO(1L, "Nova Feira", new BigDecimal("500.00"), "02/01/2025", Categoria.ALIMENTACAO);
        var dtoDetalhar = new DespesaDetalharDTO("Nova Feira", new BigDecimal("500.00"), "02/01/2025", Categoria.ALIMENTACAO);

        when(despesaService.atualizar(dtoAtualizar)).thenReturn(dtoDetalhar);

        mvc.perform(put("/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dtoAtualizar).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is(dtoDetalhar.descricao())))
                .andExpect(jsonPath("$.valor", is(dtoDetalhar.valor().doubleValue())))
                .andExpect(jsonPath("$.data", is(dtoDetalhar.data())))
                .andExpect(jsonPath("$.categoria", is(dtoDetalhar.categoria().toString())));

        verify(despesaService, times(1)).atualizar(any(DespesaAtualizarDTO.class));
    }

    @Test
    @DisplayName("Deveria devolver status 400 - Bad Request - ao tentar atualizar despesa com dados inválidos")
    @WithMockUser
    void atualizarDespesasComDadosInvalidos() throws Exception {
        var dtoAtualizar = new DespesaAtualizarDTO(1L, "", new BigDecimal("00.00"), "2015-02-01", Categoria.ALIMENTACAO);

        mvc.perform(put("/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dtoAtualizar).getJson()))
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(
                        Map.of("campo", "valor", "mensagem", "O valor deve ser maior que zero!"),
                        Map.of("campo", "data", "mensagem", "A data deve estar no formato dd/MM/yyyy!"))));
    }

    @Test
    @DisplayName("Deveria devolver status 404 - Not Found - ao tentar atualizar despesa com o id não encontrado")
    @WithMockUser
    void atualizarDespesasIdNaoEncontrado() throws Exception {
        var dtoAtualizar = new DespesaAtualizarDTO(999L, "Nova Feira", new BigDecimal("500.00"), "02/01/2025", Categoria.ALIMENTACAO);

        when(despesaService.atualizar(any(DespesaAtualizarDTO.class)))
                .thenThrow(new EntityNotFoundException("Despesa não encontrada!"));

        mvc.perform(put("/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dtoAtualizar).getJson()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Despesa não encontrada")));

        verify(despesaService, times(1)).atualizar(any(DespesaAtualizarDTO.class));
    }

    @Test
    @DisplayName("Deveria devolver status 200 - Ok - ao tentar deletar uma despesa com id encontrado")
    @WithMockUser
    void deletarDespesasIdEncontrado() throws Exception {
        doNothing().when(despesaService).deletar(anyLong());

        mvc.perform(delete("/despesas/1"))
                .andExpect(status().isNoContent());

        verify(despesaService, times(1)).deletar(anyLong());
    }

    @Test
    @DisplayName("Deveria devolver status 404 - Not Found - ao tentar deletar uma despesa com id não encontrado")
    @WithMockUser
    void deletarDespesasIdNaoEncontrado() throws Exception {
        doThrow(new EntityNotFoundException("Despesa não encontrada!"))
                .when(despesaService).deletar(anyLong());

        mvc.perform(delete("/despesas/1"))
                .andExpect(status().isNotFound());

        verify(despesaService, times(1)).deletar(anyLong());
    }

    @Test
    @DisplayName("Deveria devolver status 400 - Bad Request - ao tentar deletar uma despesa com id de tipo inválido")
    @WithMockUser
    void deletarDespesasIdTipoInvalido() throws Exception {
        doThrow(MethodArgumentTypeMismatchException.class)
                .when(despesaService).deletar(anyLong());

        mvc.perform(delete("/despesas/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("deve ser um número inteiro válido")));
    }
}