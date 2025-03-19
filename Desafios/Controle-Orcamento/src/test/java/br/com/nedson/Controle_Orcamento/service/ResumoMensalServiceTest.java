package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.model.Categoria;
import br.com.nedson.Controle_Orcamento.repository.DespesaRepository;
import br.com.nedson.Controle_Orcamento.repository.ReceitaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResumoMensalServiceTest {

    @InjectMocks
    private ResumoMensalService resumoMensalService;

    @Mock
    private ReceitaRepository receitaRepository;

    @Mock
    private DespesaRepository despesaRepository;

    @Test
    void deveGerarResumoComReceitasEDespesas() {
        // Arrange
        int ano = 2025, mes = 3;
        var totalReceitas = new BigDecimal("3000.00");
        var totalDespesas = new BigDecimal("2000.00");
        var saldoFinal = totalReceitas.subtract(totalDespesas);

        var gastosPorCategoria = List.of(
                new Object[]{Categoria.ALIMENTACAO, new BigDecimal("800.00")},
                new Object[]{Categoria.TRANSPORTE, new BigDecimal("1200.00")}
        );

        when(receitaRepository.calcularTotalReceitasPorMes(ano, mes)).thenReturn(Optional.of(totalReceitas));
        when(despesaRepository.calcularTotalDespesasPorMes(ano, mes)).thenReturn(Optional.of(totalDespesas));
        when(despesaRepository.calcularTotalPorCategoria(ano, mes)).thenReturn(gastosPorCategoria);

        var resumo = resumoMensalService.gerarResumo(ano, mes);

        assertNotNull(resumo);
        assertEquals(totalReceitas, resumo.totalReceitas());
        assertEquals(totalDespesas, resumo.totalDespesas());
        assertEquals(saldoFinal, resumo.saldoFinal());
        assertEquals(new BigDecimal("800.00"), resumo.totalGastoPorCategoria().get(Categoria.ALIMENTACAO));
        assertEquals(new BigDecimal("1200.00"), resumo.totalGastoPorCategoria().get(Categoria.TRANSPORTE));
    }

    @Test
    void deveGerarResumoComValoresZeradosQuandoNaoHaReceitasEDespesas() {
        var ano = 2024;
        var mes = 2;

        when(receitaRepository.calcularTotalReceitasPorMes(ano, mes)).thenReturn(Optional.empty());
        when(despesaRepository.calcularTotalDespesasPorMes(ano, mes)).thenReturn(Optional.empty());
        when(despesaRepository.calcularTotalPorCategoria(ano, mes)).thenReturn(Collections.emptyList());

        var resumo = resumoMensalService.gerarResumo(ano, mes);

        assertNotNull(resumo);
        assertEquals(BigDecimal.ZERO, resumo.totalReceitas());
        assertEquals(BigDecimal.ZERO, resumo.totalDespesas());
        assertEquals(BigDecimal.ZERO, resumo.saldoFinal());
        assertTrue(resumo.totalGastoPorCategoria().isEmpty());
    }

    @Test
    void deveGerarResumoQuandoHaApenasReceitas() {
        var ano = 2024;
        var mes = 2;
        var totalReceitas = new BigDecimal("1500.00");

        when(receitaRepository.calcularTotalReceitasPorMes(ano, mes)).thenReturn(Optional.of(totalReceitas));
        when(despesaRepository.calcularTotalDespesasPorMes(ano, mes)).thenReturn(Optional.empty());
        when(despesaRepository.calcularTotalPorCategoria(ano, mes)).thenReturn(Collections.emptyList());

        var resumo = resumoMensalService.gerarResumo(ano, mes);

        assertNotNull(resumo);
        assertEquals(totalReceitas, resumo.totalReceitas());
        assertEquals(BigDecimal.ZERO, resumo.totalDespesas());
        assertEquals(totalReceitas, resumo.saldoFinal());
        assertTrue(resumo.totalGastoPorCategoria().isEmpty());
    }

    @Test
    void deveGerarResumoQuandoHaApenasDespesas() {
        var ano = 2024;
        var mes = 2;
        var totalDespesas = new BigDecimal("1200.00");

        when(receitaRepository.calcularTotalReceitasPorMes(ano, mes)).thenReturn(Optional.empty());
        when(despesaRepository.calcularTotalDespesasPorMes(ano, mes)).thenReturn(Optional.of(totalDespesas));
        when(despesaRepository.calcularTotalPorCategoria(ano, mes)).thenReturn(Collections.emptyList());

        var resumo = resumoMensalService.gerarResumo(ano, mes);

        assertNotNull(resumo);
        assertEquals(BigDecimal.ZERO, resumo.totalReceitas());
        assertEquals(totalDespesas, resumo.totalDespesas());
        assertEquals(new BigDecimal("-1200.00"), resumo.saldoFinal());
        assertTrue(resumo.totalGastoPorCategoria().isEmpty());
    }
}