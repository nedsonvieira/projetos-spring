package br.com.nedson.AluraFlix.unitarios.model;

import br.com.nedson.AluraFlix.dto.categoria.DadosAtualizarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosCadastrarCategoria;
import br.com.nedson.AluraFlix.model.Categoria;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoriaTest {

    private Validator validator;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        var dados = new DadosCadastrarCategoria("Categoria Teste", "#FFFFFF");
        categoria = new Categoria(dados);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Deveria cadastrar categoria com dados válidos")
    void cadastrar_cenario1() {
        assertThat(categoria).isNotNull();
        assertThat(categoria.getTitulo()).isEqualTo("Categoria Teste");
        assertThat(categoria.getCor()).isEqualTo("#FFFFFF");
        assertThat(categoria.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deveria falhar ao cadastrar categoria sem título")
    void cadastrar_cenario2() {
        var dadosCadastrar = new DadosCadastrarCategoria("", "#FFFFFF");
        var violacoes = validator.validate(dadosCadastrar);

        assertThat(violacoes).isNotEmpty();
        assertThat(violacoes.iterator().next().getMessage()).isEqualTo("O título é obrigatório!");
    }

    @Test
    @DisplayName("Deveria falhar ao cadastrar categoria sem cor")
    void cadastrar_cenario3() {
        var dadosCadastrar = new DadosCadastrarCategoria("Categoria Teste", "");
        var violacoes = validator.validate(dadosCadastrar);

        assertThat(violacoes)
                .extracting(ConstraintViolation::getMessage)
                .contains("A cor é obrigatória!");
    }

    @Test
    @DisplayName("Deveria falhar ao cadastrar categoria com cor inválida")
    void cadastrar_cenario4() {
        var dadosCadastrar = new DadosCadastrarCategoria("Categoria Teste", "123456"); // Sem '#'
        var violacoes = validator.validate(dadosCadastrar);

        assertThat(violacoes)
                .extracting(ConstraintViolation::getMessage)
                .contains("A cor deve estar no formato hexadecimal, ex: #FFFFFF");
    }

    @Test
    @DisplayName("Deveria atualizar título e cor corretamente")
    void atualizar() {
        categoria.atualizar(new DadosAtualizarCategoria(1L, "Nova Categoria", "#AAAAAA"));

        assertThat(categoria.getTitulo()).isEqualTo("Nova Categoria");
        assertThat(categoria.getCor()).isEqualTo("#AAAAAA");
    }

    @Test
    @DisplayName("Deveria inativar a categoria corretamente")
    void inativar() {
        categoria.inativar();

        assertThat(categoria.getAtivo()).isFalse();
    }
}