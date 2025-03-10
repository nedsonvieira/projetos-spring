package br.com.nedson.AluraFlix.unitarios.service;

import br.com.nedson.AluraFlix.dto.video.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosCadastrarVideo;
import br.com.nedson.AluraFlix.model.Categoria;
import br.com.nedson.AluraFlix.model.Video;
import br.com.nedson.AluraFlix.repository.CategoriaRepository;
import br.com.nedson.AluraFlix.repository.VideoRepository;
import br.com.nedson.AluraFlix.service.VideoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class VideoServiceTest {

    @InjectMocks
    private VideoService videoService;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    private DadosCadastrarVideo dadosCadastrar;
    private Video video;
    private Long id;

    @BeforeEach
    void setup() {
        id = 1L;
        dadosCadastrar = mock(DadosCadastrarVideo.class);
        video = mock(Video.class);
    }

    @Test
    @DisplayName("Deveria cadastrar o vídeo com a categoria informada")
    void cadastrar_cenario1() {
        //Arrange
        var categoriaId = 2L;
        var url = "http://teste.com/video";

        when(dadosCadastrar.url()).thenReturn(url);
        when(dadosCadastrar.categoriaId()).thenReturn(categoriaId);
        when(videoRepository.findByUrl(url)).thenReturn(Optional.empty());

        // Simula que a categoria existe para o id informado
        var categoria = mock(Categoria.class);

        when(categoriaRepository.findByIdAndAtivoTrue(categoriaId))
                .thenReturn(Optional.of(categoria));
        when(videoRepository.save(any(Video.class))).thenReturn(video);

        // Act
        var resultado = videoService.cadastrar(dadosCadastrar);

        // Assert
        verify(videoRepository).findByUrl(url);
        verify(categoriaRepository).findByIdAndAtivoTrue(categoriaId);
        verify(videoRepository).save(any(Video.class));
        assertSame(video, resultado);
    }

    @Test
    @DisplayName("Deveria cadastrar o vídeo com a categoria livre quando a categoria não for informada")
    void cadastrar_cenario2() {
        // Arrange
        var url = "http://teste.com/video";

        when(dadosCadastrar.url()).thenReturn(url);
        when(dadosCadastrar.categoriaId()).thenReturn(null);
        when(videoRepository.findByUrl(url)).thenReturn(Optional.empty());

        var categoriaLivre = mock(Categoria.class);

        when(categoriaRepository.findByIdAndAtivoTrue(1L))
                .thenReturn(Optional.of(categoriaLivre));
        when(videoRepository.save(any(Video.class))).thenReturn(video);

        // Act
        var resultado = videoService.cadastrar(dadosCadastrar);

        // Assert
        verify(videoRepository).findByUrl(url);
        verify(categoriaRepository).findByIdAndAtivoTrue(1L);
        verify(videoRepository).save(any(Video.class));
        assertSame(video, resultado);
    }

    @Test
    @DisplayName("Deveria lançar a exceção quando já existir um vídeo cadastrado com o mesmo url")
    void cadastrar_cenario3() {
        // Arrange
        var url = "http://teste.com/video";

        when(dadosCadastrar.url()).thenReturn(url);
        when(videoRepository.findByUrl(url)).thenReturn(Optional.of(video));

        // Act e Assert
        assertThatThrownBy(() -> videoService.cadastrar(dadosCadastrar))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Já existe um vídeo salvo com este url!");
    }

    @Test
    @DisplayName("Deveria lançar a exceção quando a categoria não for informada e categoria livre não existir")
    void cadastrar_cenario4() {
        var url = "http://teste.com/video";

        when(dadosCadastrar.url()).thenReturn(url);
        when(dadosCadastrar.categoriaId()).thenReturn(null);
        when(videoRepository.findByUrl(url)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> videoService.cadastrar(dadosCadastrar))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Categoria Livre não existe!");
    }

    @Test
    @DisplayName("Deveria devolver a lista com os vídeos cadastrados")
    void listar() {
        // Arrange
        var categoria = mock(Categoria.class);
        var paginacao = PageRequest.of(0, 5);
        var listaVideos = List.of(new Video(dadosCadastrar, categoria));
        var page = new PageImpl<>(listaVideos, paginacao, listaVideos.size());

        when(videoRepository.findAllByAtivoTrue(paginacao)).thenReturn(page);

        // Act
        var resultado = videoService.listar(paginacao);

        // Assert
        verify(videoRepository).findAllByAtivoTrue(paginacao);
        assertEquals(listaVideos.size(), resultado.getContent().size());
        // Para cada instância mapeada, pode verificar se não está nula ou testar atributos específicos,
        resultado.getContent().forEach(Assertions::assertNotNull);
    }

    @Test
    @DisplayName("Deveria devolver os detalhes do vídeo buscado")
    void listarById_cenario1() {

        when(videoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(video));
        when(videoRepository.getReferenceById(id)).thenReturn(video);

        //Act
        var resultado = videoService.listarById(id);

        // Assert
        verify(videoRepository).findByIdAndAtivoTrue(id);
        verify(videoRepository).getReferenceById(id);
        assertSame(video, resultado);
    }

    @Test
    @DisplayName("Deveria lançar exceção quando o vídeo não for encontrado")
    void listarById_cenario2() {
        when(videoRepository.existsByIdAndAtivoTrue(id)).thenReturn(false);

        // Act e Assert
        assertThatThrownBy(() -> videoService.listarById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vídeo não encontrado!");
    }

    @Test
    @DisplayName("Deveria atualizar o vídeo com sucesso")
    void atualizar_cenario1() {

        var dados = mock(DadosAtualizarVideo.class);
        when(dados.id()).thenReturn(id);

        when(videoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(video));
        when(videoRepository.getReferenceById(id)).thenReturn(video);

        var dadosAtualizar = new DadosAtualizarVideo(id, "Novo Vídeo", "Descrição do vídeo novo", "http://teste.com/novo-video");
        video.atualizar(dadosAtualizar);

        //Act
        var resultado = videoService.atualizar(dados);

        //Assert
        verify(video, times(1)).atualizar(dados);
        assertSame(video, resultado);
    }

    @Test
    @DisplayName("Deveria lançar exceção ao tentar atualizar vídeo inexistente")
    void atualizar_cenario2() {
        when(videoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.empty());

        // Act e Assert
        assertThatThrownBy(() -> videoService.listarById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vídeo não encontrado!");
    }

    @Test
    @DisplayName("Deveria deletar o vídeo com sucesso")
    void deletar_cenario1() {

        when(videoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(video));
        when(videoRepository.getReferenceById(id)).thenReturn(video);

        //Act
        videoService.deletar(id);

        //Assert
        verify(video, times(1)).inativar();
    }

    @Test
    @DisplayName("Deveria lançar ao tentar deletar vídeo inexistente")
    void deletar_cenario2() {
        when(videoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.empty());

        // Act e Assert
        assertThatThrownBy(() -> videoService.deletar(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vídeo não encontrado!");
    }
}