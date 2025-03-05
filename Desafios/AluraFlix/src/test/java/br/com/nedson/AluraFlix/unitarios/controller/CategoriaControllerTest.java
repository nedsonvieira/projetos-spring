package br.com.nedson.AluraFlix.unitarios.controller;

import br.com.nedson.AluraFlix.dto.categoria.DadosAtualizarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosBuscarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosCadastrarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosDetalharCategoria;
import br.com.nedson.AluraFlix.model.Categoria;
import br.com.nedson.AluraFlix.repository.CategoriaRepository;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaRepository categoriaRepository;

    @Autowired
    private JacksonTester<DadosCadastrarCategoria> jsonCadastrar;

    @Autowired
    private JacksonTester<DadosAtualizarCategoria> jsonAtualizar;

    @Autowired
    private JacksonTester<DadosDetalharCategoria> jsonDetalhar;

    @Autowired
    private JacksonTester<Page<DadosBuscarCategoria>> jsonBuscar;

    @Test
    @DisplayName("Deveria cadastrar uma categoria com sucesso e devolver status 201 e a categoria cadastrada")
    void cadastrar_cenario1() throws Exception {
        var dados = new DadosCadastrarCategoria("Categoria Teste", "#FFFFFF");
        var categoria = new Categoria(dados);

        when(categoriaRepository.existsByTitulo(dados.titulo())).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        var response = mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dados).getJson()))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        var dadosDetalhar = new DadosDetalharCategoria(categoria);
        var jsonEsperado = jsonDetalhar.write(dadosDetalhar).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver erro 400 ao tentar cadastrar uma categoria com dados inválidos")
    void cadastrar_cenario2() throws Exception {
        var dados = new DadosCadastrarCategoria("", "");

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dados).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deveria listar categorias ativas e devolver status 200")
    void listar_cenario1() throws Exception {
        var categoria = new Categoria(1L, "Categoria Livre", "#FFFFFF", true);
        var page = new PageImpl<>(List.of(categoria));

        when(categoriaRepository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/categorias")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                // Verifica se o conteúdo retornado não está vazio.
                .andExpect(jsonPath("$.content", not(empty())));
    }

    @Test
    @DisplayName("Deveria listar os detalhes de uma categoria ao buscar por ID e devolver status 200")
    void listarById_cenario1() throws Exception {
        var categoria = new Categoria(1L, "Categoria Livre", "#FFFFFF", true);

        when(categoriaRepository.findByIdAndAtivoTrue(categoria.getId())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.getReferenceById(categoria.getId())).thenReturn(categoria);

        var response = mockMvc.perform(get("/categorias/{id}", categoria.getId().intValue()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var dadosDetalhar = new DadosDetalharCategoria(categoria);
        var jsonEsperado = jsonDetalhar.write(dadosDetalhar).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver erro 404 ao buscar uma categoria inexistente")
    void listarById_cenario2() throws Exception {
        when(categoriaRepository.findAtivoById(999L)).thenReturn(false);

        mockMvc.perform(get("/categorias/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Categoria não encontrada")));
    }

    @Test
    @DisplayName("Deveria atualizar uma categoria com sucesso e devolver status 200 e os detalhes da categoria atualizada")
    void atualizar_cenario1() throws Exception {
        var categoria = new Categoria(1L, "Categoria Teste", "#FFFFFF", true);
        var dadosAtualizar = new DadosAtualizarCategoria(categoria.getId(), "Nova Categoria", "#AAAAAA");

        when(categoriaRepository.findByIdAndAtivoTrue(categoria.getId())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.getReferenceById(categoria.getId())).thenReturn(categoria);

        var response = mockMvc.perform(put("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dadosAtualizar).getJson()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var dadosDetalhar = new DadosDetalharCategoria(categoria);
        var jsonEsperado = jsonDetalhar.write(dadosDetalhar).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver erro 404 ao tentar atualizar uma categoria inexistente")
    void atualizar_cenario2() throws Exception {
        var dadosAtualizar = new DadosAtualizarCategoria(999L, "Nova Categoria", "#AAAAAA");

        when(categoriaRepository.findAtivoById(999L)).thenReturn(false);

        var response = mockMvc.perform(put("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dadosAtualizar).getJson()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Categoria não encontrada")));
    }

    @Test
    @DisplayName("Deveria deletar uma categoria com sucesso e retornar a mensagem")
    void deletar_cenario1() throws Exception {
        var categoria = new Categoria(1L, "Categoria Livre", "#FFFFFF", true);

        when(categoriaRepository.findByIdAndAtivoTrue(categoria.getId())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.getReferenceById(categoria.getId())).thenReturn(categoria);

        var response = mockMvc.perform(delete("/categorias/{id}", categoria.getId().intValue()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isEqualTo("Categoria deletada com sucesso!");
    }

    @Test
    @DisplayName("Deveria retornar erro 404 ao tentar deletar uma categoria inexistente")
    void deletar_cenario2() throws Exception {
        mockMvc.perform(delete("/categorias/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Categoria não encontrada")));
    }
}
