package br.com.nedson.Controle_Orcamento.model;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DespesaTest {

    @Test
    @DisplayName("Deve criar despesa com todos os campos preenchidos")
    void deveCriarDespesaComTodosOsCampos() {
        var dto = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/03/2024", Categoria.MORADIA);

        var despesa = new Despesa(dto);

        assertEquals("Aluguel", despesa.getDescricao());
        assertEquals(new BigDecimal("1200.00"), despesa.getValor());
        assertEquals(LocalDate.parse("10/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), despesa.getData());
        assertEquals(Categoria.MORADIA, despesa.getCategoria());
    }

    @Test
    @DisplayName("Deve criar despesa com categoria padrão quando ela não for fornecida")
    void deveCriarDespesaComCategoriaPadraoQuandoNaoForFornecida() {
        var dto = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/03/2024", null);

        var despesa = new Despesa(dto);

        assertEquals("Aluguel", despesa.getDescricao());
        assertEquals(new BigDecimal("1200.00"), despesa.getValor());
        assertEquals(LocalDate.parse("10/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), despesa.getData());
        assertEquals(Categoria.OUTRAS, despesa.getCategoria());
    }

    @Test
    @DisplayName("Deve atualizar todos os campos de uma despesa existente")
    void deveAtualizarTodosOsCamposDeUmaDespesa() {
        var dto = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/03/2024", Categoria.MORADIA);
        var dtoAtualizar = new DespesaAtualizarDTO(1L, "Novo Aluguel", new BigDecimal("1400.00"), "15/03/2024", Categoria.MORADIA);

        var despesa = new Despesa(dto);

        despesa.atualizar(dtoAtualizar);

        assertEquals("Novo Aluguel", despesa.getDescricao());
        assertEquals(new BigDecimal("1400.00"), despesa.getValor());
        assertEquals(LocalDate.parse("15/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), despesa.getData());
        assertEquals(Categoria.MORADIA, despesa.getCategoria());
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos fornecidos no DTO")
    void deveAtualizarApenasOsCamposFornecidos() {

        var dto = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/03/2024", Categoria.MORADIA);
        var dtoAtualizar = new DespesaAtualizarDTO(1L, null, new BigDecimal("1400.00"), null, Categoria.TRANSPORTE);

        var despesa = new Despesa(dto);

        despesa.atualizar(dtoAtualizar);

        assertEquals("Aluguel", despesa.getDescricao());
        assertEquals(new BigDecimal("1400.00"), despesa.getValor());
        assertEquals(LocalDate.parse("10/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), despesa.getData());
        assertEquals(Categoria.TRANSPORTE, despesa.getCategoria());
    }

    @Test
    @DisplayName("Não deve atualizar nenhum campo quando todos os valores do DTO forem nulos")
    void naoDeveAtualizarNenhumCampoQuandoDtoForNulo() {
        var dto = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/03/2024", Categoria.MORADIA);

        var dtoAtualizar = new DespesaAtualizarDTO(1L, null, null, null, null);

        var despesa = new Despesa(dto);

        despesa.atualizar(dtoAtualizar);

        assertEquals("Aluguel", despesa.getDescricao());
        assertEquals(new BigDecimal("1200.00"), despesa.getValor());
        assertEquals(LocalDate.parse("10/03/2024", DateTimeFormatter.ofPattern("dd/MM/yyyy")), despesa.getData());
        assertEquals(Categoria.MORADIA, despesa.getCategoria());

    }
}