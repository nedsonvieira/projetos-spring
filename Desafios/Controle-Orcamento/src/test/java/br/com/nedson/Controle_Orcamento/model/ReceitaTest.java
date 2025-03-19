package br.com.nedson.Controle_Orcamento.model;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceitaTest {

    @Test
    @DisplayName("Deve criar despesa com todos os campos preenchidos")
    void deveCriarReceitaComTodosOsCampos() {
        var dto = new ReceitaCadastrarDTO("Salário", new BigDecimal("1200.00"), "10/03/2024");

        var receita = new Receita(dto);

        assertEquals("Salário", receita.getDescricao());
        assertEquals(new BigDecimal("1200.00"), receita.getValor());
        assertEquals(LocalDate.parse("10/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), receita.getData());
    }

    @Test
    @DisplayName("Deve atualizar todos os campos de uma receita existente")
    void deveAtualizarTodosOsCampos() {
        var dto = new ReceitaCadastrarDTO("Salário", new BigDecimal("1200.00"), "10/03/2024");
        var dtoAtualizar = new ReceitaAtualizarDTO(1L, "Novo Salário", new BigDecimal("1400.00"), "15/03/2024");

        var receita = new Receita(dto);

        receita.atualizar(dtoAtualizar);

        assertEquals("Novo Salário", receita.getDescricao());
        assertEquals(new BigDecimal("1400.00"), receita.getValor());
        assertEquals(LocalDate.parse("15/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), receita.getData());
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos fornecidos no DTO")
    void deveAtualizarCamposFornecidos() {
        var dto = new ReceitaCadastrarDTO("Salário", new BigDecimal("1200.00"), "10/03/2024");
        var dtoAtualizar = new ReceitaAtualizarDTO(1L, null, new BigDecimal("1400.00"), null);

        var receita = new Receita(dto);

        receita.atualizar(dtoAtualizar);

        assertEquals("Salário", receita.getDescricao());
        assertEquals(new BigDecimal("1400.00"), receita.getValor());
        assertEquals(LocalDate.parse("10/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), receita.getData());
    }

    @Test
    @DisplayName("Não deve atualizar nenhum campo quando todos os valores do DTO forem nulos")
    void naoDeveAtualizarCamposQuandoDtoForNulo() {
        var dto = new ReceitaCadastrarDTO("Salário", new BigDecimal("1200.00"), "10/03/2024");
        var dtoAtualizar = new ReceitaAtualizarDTO(1L, null, null, null);

        var receita = new Receita(dto);

        receita.atualizar(dtoAtualizar);

        assertEquals("Salário", receita.getDescricao());
        assertEquals(new BigDecimal("1200.00"), receita.getValor());
        assertEquals(LocalDate.parse("10/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), receita.getData());
    }

}