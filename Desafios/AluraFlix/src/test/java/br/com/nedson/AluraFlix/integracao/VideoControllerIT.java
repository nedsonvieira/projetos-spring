package br.com.nedson.AluraFlix.integracao;

import br.com.nedson.AluraFlix.dto.categoria.DadosCadastrarCategoria;
import br.com.nedson.AluraFlix.dto.video.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosBuscarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosCadastrarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosDetalharVideo;
import br.com.nedson.AluraFlix.model.Categoria;
import br.com.nedson.AluraFlix.model.Video;
import br.com.nedson.AluraFlix.repository.CategoriaRepository;
import br.com.nedson.AluraFlix.repository.VideoRepository;
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
import org.springframework.data.domain.Page;

import static org.hamcrest.Matchers.*;

import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.json.JacksonTester;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
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
public class VideoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // JacksonTester para os records
    @Autowired
    private JacksonTester<DadosCadastrarVideo> jsonCadastrar;

    @Autowired
    private JacksonTester<DadosAtualizarVideo> jsonAtualizar;

    @Autowired
    private JacksonTester<DadosDetalharVideo> jsonDetalhar;

    @Autowired
    private JacksonTester<Page<DadosBuscarVideo>> jsonBuscar;

    private DadosCadastrarVideo dadosCadastrar;
    private Video video;

    @BeforeEach
    void setUp() {
        // Inicializa os JacksonTester
        JacksonTester.initFields(this, objectMapper);

        // Limpa os repositórios antes de cada teste
        videoRepository.deleteAll();
        categoriaRepository.deleteAll();

        jdbcTemplate.execute("ALTER TABLE categorias AUTO_INCREMENT = 1");
        jdbcTemplate.execute("ALTER TABLE videos AUTO_INCREMENT = 1");

        var dadosCadastrarCategoria = new DadosCadastrarCategoria("Livre", "#FFFFFF");
        var categoria = new Categoria(dadosCadastrarCategoria);
        // Categoria Livre que será utilizada quando nenhum id for informado
        Categoria categoriaLivre = categoriaRepository.save(categoria);

        dadosCadastrar = new DadosCadastrarVideo(
                "Título Teste",
                "Descrição Teste",
                "http://video.com/teste",
                null
        );

        video = new Video(dadosCadastrar, categoriaLivre);
    }

    @Test
    @DisplayName("Teste de integração: Cadastrar vídeo com sucesso")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dadosCadastrar).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is(video.getTitulo())))
                .andExpect(jsonPath("$.descricao", is(video.getDescricao())))
                .andExpect(jsonPath("$.url", is(video.getUrl())))
                .andExpect(jsonPath("$.categoriaId", is(video.getCategoria().getId().intValue())));

        assertThat(videoRepository.findAll().getFirst()).isNotNull();
    }

    @Test
    @DisplayName("Teste de integração: Cadastrar vídeo com categoria informada")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
        var novosDadosCategoria = new DadosCadastrarCategoria("Documentário", "#FFFFFF");
        var categoria = new Categoria(novosDadosCategoria);
        categoria = categoriaRepository.save(categoria);

        dadosCadastrar = new DadosCadastrarVideo(
                "Título Teste",
                "Descrição Teste",
                "http://video.com/teste",
                categoria.getId()
        );

        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dadosCadastrar).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is(dadosCadastrar.titulo())))
                .andExpect(jsonPath("$.descricao", is(dadosCadastrar.descricao())))
                .andExpect(jsonPath("$.url", is(dadosCadastrar.url())))
                .andExpect(jsonPath("$.categoriaId", is(categoria.getId().intValue())));

        assertThat(videoRepository.findAll().getFirst()).isNotNull();
    }

    @Test
    @DisplayName("Teste de integração: Cadastrar vídeo com categoria não informada e categoria Livre não salva")
    @WithMockUser
    void cadastrar_cenario3() throws Exception {
        categoriaRepository.deleteAll();

        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dadosCadastrar).getJson()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Categoria Livre não existe!")));

        assertThat(categoriaRepository.findByIdAndAtivoTrue(1L)).isEmpty();
    }

    @Test
    @DisplayName("Teste de integração: Cadastrar vídeo com dados inválidos")
    @WithMockUser
    void cadastrar_cenario4() throws Exception {
        var dadosInvalidos = new DadosCadastrarVideo("", "", "url-invalido", null);

        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dadosInvalidos).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*]").value(containsInAnyOrder(
                        Map.of("campo", "url", "mensagem", "O formato do URL é inválido!"),
                        Map.of("campo", "titulo", "mensagem", "O título é obrigatório!"),
                        Map.of("campo", "descricao", "mensagem", "A descrição é obrigatória!")
                )));

        assertThat(videoRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Teste de integração: Listar vídeos paginados")
    @WithMockUser
    void listar() throws Exception {
        videoRepository.save(video);

        mockMvc.perform(get("/videos")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                // Verifica se o conteúdo retornado não está vazio.
                .andExpect(jsonPath("$.content", not(empty())));
    }

    @Test
    @DisplayName("Teste de integração: Buscar vídeo por ID")
    @WithMockUser
    void listarById_cenario1() throws Exception {
        video = videoRepository.save(video);

        mockMvc.perform(get("/videos/" + video.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(video.getId().intValue())))
                .andExpect(jsonPath("$.titulo", is(video.getTitulo())))
                .andExpect(jsonPath("$.descricao", is(video.getDescricao())))
                .andExpect(jsonPath("$.url", is(video.getUrl())))
                .andExpect(jsonPath("$.categoriaId", is(video.getCategoria().getId().intValue())));

        assertThat(videoRepository.findByIdAndAtivoTrue(video.getId())).isPresent();
    }

    @Test
    @DisplayName("Teste de integração: Buscar vídeo inexistente")
    @WithMockUser
    void listarById_cenario2() throws Exception {
        mockMvc.perform(get("/videos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Vídeo não encontrado")));

        assertThat(videoRepository.findByIdAndAtivoTrue(999L)).isEmpty();
    }

    @Test
    @DisplayName("Teste de integração: Atualizar vídeo")
    @WithMockUser
    void atualizar_cenario1() throws Exception {
        video = videoRepository.save(video);
        var dadosAtualizar = new DadosAtualizarVideo(video.getId(), "Novo Vídeo", "Descrição do vídeo novo", "http://teste.com/novo-video");

        mockMvc.perform(put("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dadosAtualizar).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dadosAtualizar.id().intValue())))
                .andExpect(jsonPath("$.titulo", is(dadosAtualizar.titulo())))
                .andExpect(jsonPath("$.descricao", is(dadosAtualizar.descricao())))
                .andExpect(jsonPath("$.url", is(dadosAtualizar.url())))
                .andExpect(jsonPath("$.categoriaId", is(video.getCategoria().getId().intValue())));

        assertThat(videoRepository.findByIdAndAtivoTrue(video.getId())).isPresent();
    }

    @Test
    @DisplayName("Teste de integração: Atualizar vídeo inexistente")
    @WithMockUser
    void atualizar_cenario2() throws Exception {
        var dadosAtualizar = new DadosAtualizarVideo(
                999L,
                "Novo Vídeo",
                "Descrição do vídeo novo",
                "http://teste.com/novo-video"
        );
        mockMvc.perform(put("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dadosAtualizar).getJson()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Vídeo não encontrado")));
    }

    @Test
    @DisplayName("Teste de integração: Deletar vídeo")
    @WithMockUser
    void deletar_cenario1() throws Exception {
        videoRepository.save(video);

        mockMvc.perform(delete("/videos/{id}", video.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(containsString("Vídeo deletado com sucesso!")));

        assertThat(videoRepository.findByIdAndAtivoTrue(video.getId())).isEmpty();
    }

    @Test
    @DisplayName("Teste de integração: Deletar vídeo inexistente")
    @WithMockUser
    void deletar_cenario2() throws Exception {
        mockMvc.perform(delete("/videos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Vídeo não encontrado")));
    }
}