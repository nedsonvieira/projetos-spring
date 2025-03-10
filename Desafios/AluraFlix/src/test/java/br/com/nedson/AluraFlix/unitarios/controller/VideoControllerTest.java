package br.com.nedson.AluraFlix.unitarios.controller;

import br.com.nedson.AluraFlix.dto.video.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosBuscarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosCadastrarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosDetalharVideo;
import br.com.nedson.AluraFlix.model.Categoria;
import br.com.nedson.AluraFlix.model.Video;
import br.com.nedson.AluraFlix.service.VideoService;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VideoService videoService;

    @Autowired
    private JacksonTester<DadosCadastrarVideo> jsonCadastrar;

    @Autowired
    private JacksonTester<DadosAtualizarVideo> jsonAtualizar;

    @Autowired
    private JacksonTester<DadosDetalharVideo> jsonDetalhar;

    @Autowired
    private JacksonTester<Page<DadosBuscarVideo>> jsonBuscar;

    private Video video;
    private DadosCadastrarVideo dadosCadastrar;
    private Long id;

    @BeforeEach
    void setup(){
        id = 1L;
        dadosCadastrar = new DadosCadastrarVideo("Vídeo Teste", "Descrição do vídeo", "http://teste.com/video", null);
        var categoria = new Categoria(id, "Categoria Teste", "#FFFFFF", true);
        video = new Video(dadosCadastrar, categoria);
    }

    @Test
    @DisplayName("Deveria cadastrar um vídeo com sucesso e devolver status 201 e o vídeo cadastrado")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        when(videoService.cadastrar(any())).thenReturn(video);

        var response = mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dadosCadastrar).getJson()))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        var dadosDetalhar = new DadosDetalharVideo(video);
        var jsonEsperado = jsonDetalhar.write(dadosDetalhar).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver erro 400 ao tentar cadastrar um vídeo com dados inválidos")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
        var dados = new DadosCadastrarVideo("", "", "url-invalido", null);

        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCadastrar.write(dados).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deveria devolver status 200 e retornar a lista de vídeos")
    @WithMockUser
    void listar() throws Exception {
        var page = new PageImpl<>(List.of(new DadosBuscarVideo(video)));

        when(videoService.listar(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/videos")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                // Verifica se o conteúdo retornado não está vazio.
                .andExpect(jsonPath("$.content", not(empty())));
    }

    @Test
    @DisplayName("Deveria devolver status 200 e listar os detalhes do vídeo buscado")
    @WithMockUser
    void listarById_cenario1() throws Exception {
        when(videoService.listarById(id)).thenReturn(video);

        var response = mockMvc.perform(get("/videos/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var dadosDetalhar = new DadosDetalharVideo(video);
        var jsonEsperado = jsonDetalhar.write(dadosDetalhar).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver erro 404 ao buscar um vídeo inexistente")
    @WithMockUser
    void listarById_cenario2() throws Exception {
        doThrow(new EntityNotFoundException("Vídeo não encontrado!"))
                .when(videoService).listarById(999L);

        mockMvc.perform(get("/videos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Vídeo não encontrado")));
    }

    @Test
    @DisplayName("Deveria atualizar um vídeo com sucesso e devolver status 200 e os detalhes do vídeo atualizado")
    @WithMockUser
    void atualizar_cenario1() throws Exception {
        var dadosAtualizar = new DadosAtualizarVideo(id, "Novo Vídeo", "Descrição do vídeo novo", "http://teste.com/novo-video");
        video.atualizar(dadosAtualizar);

        when(videoService.atualizar(any())).thenReturn(video);

        var response = mockMvc.perform(put("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dadosAtualizar).getJson()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var dadosDetalhar = new DadosDetalharVideo(video);
        var jsonEsperado = jsonDetalhar.write(dadosDetalhar).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver erro 404 ao tentar atualizar um vídeo inexistente")
    @WithMockUser
    void atualizar_cenario2() throws Exception {
        var dadosAtualizar = new DadosAtualizarVideo(
                999L,
                "Novo Vídeo",
                "Descrição do vídeo novo",
                "http://teste.com/novo-video"
        );
        doThrow(new EntityNotFoundException("Vídeo não encontrado!"))
                .when(videoService).atualizar(dadosAtualizar);

        mockMvc.perform(put("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizar.write(dadosAtualizar).getJson()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Vídeo não encontrado")));
    }

    @Test
    @DisplayName("Deveria deletar um vídeo com sucesso e retornar status 204 e a mensagem")
    @WithMockUser
    void deletar_cenario1() throws Exception {

        when(videoService.listarById(id)).thenReturn(video);

        mockMvc.perform(delete("/videos/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(containsString("Vídeo deletado com sucesso")));
    }

    @Test
    @DisplayName("Deveria retornar erro 404 ao tentar deletar um vídeo inexistente")
    @WithMockUser
    void deletar_cenario2() throws Exception {

        doThrow(new EntityNotFoundException("Vídeo não encontrado!"))
                .when(videoService).deletar(999L);

        mockMvc.perform(delete("/videos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Vídeo não encontrado")));
    }
}