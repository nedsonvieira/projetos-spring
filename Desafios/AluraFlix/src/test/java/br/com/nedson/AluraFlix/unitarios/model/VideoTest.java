package br.com.nedson.AluraFlix.unitarios.model;

import br.com.nedson.AluraFlix.dto.video.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosCadastrarVideo;
import br.com.nedson.AluraFlix.model.Categoria;
import br.com.nedson.AluraFlix.model.Video;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VideoTest {

    private Validator validator;
    private Video video;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1L, "Categoria Teste", "#FFFFFF", true);
        var dados = new DadosCadastrarVideo(
                "Vídeo Teste",
                "Descrição do vídeo",
                "http://teste.com/video",
                categoria.getId());
        video = new Video(dados, categoria);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Deve cadastrar um vídeo corretamente com os dados fornecidos")
    void cadastrar_cenario1(){
        assertThat(video).isNotNull();
        assertThat(video.getTitulo()).isEqualTo("Vídeo Teste");
        assertThat(video.getDescricao()).isEqualTo("Descrição do vídeo");
        assertThat(video.getCategoria()).isEqualTo(categoria);
        assertThat(video.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deveria falhar ao cadastrar vídeo sem título")
    void cadastrar_cenario2(){
        var dadosCadastrar = new DadosCadastrarVideo("", "Descrição do vídeo", "http://teste.com/video", 1L);
        var violacoes = validator.validate(dadosCadastrar);

        assertThat(violacoes).isNotEmpty();
        assertThat(violacoes.iterator().next().getMessage()).isEqualTo("O título é obrigatório!");
    }

    @Test
    @DisplayName("Deveria falhar ao cadastrar vídeo sem descrição")
    void cadastrar_cenario3(){
        var dadosCadastrar = new DadosCadastrarVideo("Vídeo Teste", "", "http://teste.com/video", 1L);
        var violacoes = validator.validate(dadosCadastrar);

        assertThat(violacoes).isNotEmpty();
        assertThat(violacoes.iterator().next().getMessage()).isEqualTo("A descrição é obrigatória!");
    }

    @Test
    @DisplayName("Deveria falhar ao cadastrar vídeo sem url")
    void cadastrar_cenario4(){
        var dadosCadastrar = new DadosCadastrarVideo("Vídeo Teste", "Descrição do vídeo", "", 1L);
        var violacoes = validator.validate(dadosCadastrar);

        assertThat(violacoes)
                .extracting(ConstraintViolation::getMessage)
                        .contains("O URL é obrigatório!");
    }

    @Test
    @DisplayName("Deveria falhar ao cadastrar vídeo com url inválido")
    void cadastrar_cenario5(){
        var dadosCadastrar = new DadosCadastrarVideo("Vídeo Teste", "Descrição do vídeo", "teste.com/video", 1L);
        var violacoes = validator.validate(dadosCadastrar);

        assertThat(violacoes)
                .extracting(ConstraintViolation::getMessage)
                .contains("O formato do URL é inválido!");
    }

    @Test
    @DisplayName("Deve atualizar as informações do vídeo corretamente")
    void atualizar() {
        video.atualizar(new DadosAtualizarVideo(1L, "Novo Vídeo", "Descrição do vídeo novo", "http://teste.com/novo-video"));

        assertThat(video.getTitulo()).isEqualTo("Novo Vídeo");
        assertThat(video.getDescricao()).isEqualTo("Descrição do vídeo novo");
        assertThat(video.getUrl()).isEqualTo("http://teste.com/novo-video");
        assertThat(video.getCategoria()).isEqualTo(categoria);
    }

    @Test
    @DisplayName("Deve inativar o vídeo corretamente")
    void inativar() {
        video.inativar();
        assertThat(video.getAtivo()).isFalse();
    }
}