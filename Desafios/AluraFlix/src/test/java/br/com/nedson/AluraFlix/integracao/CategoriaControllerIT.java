package br.com.nedson.AluraFlix.integracao;

import br.com.nedson.AluraFlix.dto.categoria.DadosAtualizarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosBuscarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosCadastrarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosDetalharCategoria;
import br.com.nedson.AluraFlix.model.Categoria;
import br.com.nedson.AluraFlix.repository.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class CategoriaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JacksonTester<DadosCadastrarCategoria> jsonCadastrar;

    @Autowired
    private JacksonTester<DadosAtualizarCategoria> jsonAtualizar;

    @Autowired
    private JacksonTester<DadosDetalharCategoria> jsonDetalhar;

    @Autowired
    private JacksonTester<Page<DadosBuscarCategoria>> jsonBuscar;

    private DadosCadastrarCategoria dados;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        // Inicializa os JacksonTester
        JacksonTester.initFields(this, objectMapper);

        // Limpa os repositórios antes de cada teste
        categoriaRepository.deleteAll();

        jdbcTemplate.execute("ALTER TABLE categorias AUTO_INCREMENT = 1");

        dados = new DadosCadastrarCategoria("Categoria Teste", "#FFFFFF");
        categoria = new Categoria(dados);
    }

    @Test
    @DisplayName("Teste de integração: Cadastrar categoria com sucesso")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dados).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is(categoria.getTitulo())))
                .andExpect(jsonPath("$.cor", is(categoria.getCor())));

        assertThat(categoriaRepository.findAll().getFirst()).isNotNull();
    }

    @Test
    @DisplayName("Teste de integração: Cadastrar categoria com dados inválidos")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
        var dadosInvalidos = new DadosCadastrarCategoria("", "111222");

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dadosInvalidos).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(
                        Map.of("campo", "titulo", "mensagem", "O título é obrigatório!"),
                        Map.of("campo", "cor", "mensagem", "A cor deve estar no formato hexadecimal, ex: #FFFFFF")
                )));
    }

    @Test
    @DisplayName("Teste de integração: Listar categorias paginadas")
    @WithMockUser
    void listar() throws Exception {
        categoriaRepository.save(categoria);

        mockMvc.perform(get("/categorias")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())));
    }

    @Test
    @DisplayName("Teste de integração: Buscar categoria por ID")
    @WithMockUser
    void listarById_cenario1() throws Exception {
        categoria = categoriaRepository.save(categoria);

        mockMvc.perform(get("/categorias/" + categoria.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoria.getId().intValue())))
                .andExpect(jsonPath("$.titulo", is(categoria.getTitulo())))
                .andExpect(jsonPath("$.cor", is(categoria.getCor())));

        assertThat(categoriaRepository.findByIdAndAtivoTrue(categoria.getId())).isPresent();
    }

    @Test
    @DisplayName("Teste de integração: Buscar categoria inexistente")
    @WithMockUser
    void listarById_cenario2() throws Exception {
        mockMvc.perform(get("/categorias/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Categoria não encontrada")));

        assertThat(categoriaRepository.findByIdAndAtivoTrue(999L)).isEmpty();
    }

    @Test
    @DisplayName("Teste de integração: Atualizar categoria")
    @WithMockUser
    void atualizar_cenario1() throws Exception {
        categoria = categoriaRepository.save(categoria);
        var dadosAtualizar = new DadosAtualizarCategoria(categoria.getId(), "Nova Categoria", "#AAAAAA");

        var response = mockMvc.perform(put("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dadosAtualizar).getJson()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var jsonResponse = response.getContentAsString();
        var categoriaAtualizada = objectMapper.readValue(jsonResponse, DadosDetalharCategoria.class);

        // Valida o resultado retornado
        assertThat(categoriaAtualizada.titulo()).isEqualTo("Nova Categoria");
        assertThat(categoriaAtualizada.cor()).isEqualTo("#AAAAAA");
    }

    @Test
    @DisplayName("Teste de integração: Atualizar categoria inexistente")
    @WithMockUser
    void atualizar_cenario2() throws Exception {
        var dadosAtualizar = new DadosAtualizarCategoria(999L, "Nova Categoria", "#AAAAAA");

        mockMvc.perform(put("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dadosAtualizar).getJson()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Categoria não encontrada")));
    }

    @Test
    @DisplayName("Teste de integração: Deletar categoria")
    @WithMockUser
    void deletar_cenario1() throws Exception {
        categoriaRepository.save(categoria);

        mockMvc.perform(delete("/categorias/{id}", categoria.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(containsString("Categoria deletada com sucesso!")));

        assertThat(categoriaRepository.findByIdAndAtivoTrue(categoria.getId())).isEmpty();
    }

    @Test
    @DisplayName("Teste de integração: Deletar categoria inexistente")
    @WithMockUser
    void deletar_cenario2() throws Exception {
        mockMvc.perform(delete("/categorias/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Categoria não encontrada")));
    }
}
