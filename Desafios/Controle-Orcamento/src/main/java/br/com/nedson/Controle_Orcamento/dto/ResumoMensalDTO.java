package br.com.nedson.Controle_Orcamento.dto;

import br.com.nedson.Controle_Orcamento.model.Categoria;

import java.math.BigDecimal;
import java.util.Map;

public record ResumoMensalDTO(

        BigDecimal totalReceitas,

        BigDecimal totalDespesas,

        BigDecimal saldoFinal,

        Map<Categoria, BigDecimal> totalGastoPorCategoria
) {
}
