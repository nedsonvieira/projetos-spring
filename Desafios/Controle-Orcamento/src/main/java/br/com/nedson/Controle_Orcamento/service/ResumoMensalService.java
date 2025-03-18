package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.dto.ResumoMensalDTO;
import br.com.nedson.Controle_Orcamento.model.Categoria;
import br.com.nedson.Controle_Orcamento.repository.DespesaRepository;
import br.com.nedson.Controle_Orcamento.repository.ReceitaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResumoMensalService {

    private final ReceitaRepository receitaRepository;

    private final DespesaRepository despesaRepository;

    public ResumoMensalDTO gerarResumo(int ano, int mes) {
        var totalReceitas = receitaRepository.calcularTotalReceitasPorMes(ano, mes)
                .orElse(BigDecimal.ZERO);
        var totalDespesas = despesaRepository.calcularTotalDespesasPorMes(ano, mes)
                .orElse(BigDecimal.ZERO);
        var saldoFinal = totalReceitas.subtract(totalDespesas);

        /*
 Transformar lista de Objects em Map de Categoria e BigDecimal
        Map<Categoria, BigDecimal> totalGastoPorCategoria = new HashMap<>();

        var resultados = despesaRepository.calcularTotalPorCategoria(ano, mes);
        for (Object[] objects : resultados) {
            Categoria categoria = (Categoria) objects[0];
            BigDecimal total = (BigDecimal) objects[1];
            totalGastoPorCategoria.merge(categoria, total, BigDecimal::add);
        }
*/
        var totalGastoPorCategoria = despesaRepository
                .calcularTotalPorCategoria(ano, mes)
                .stream().collect(Collectors.toMap(
                        linha -> (Categoria) linha[0],
                        linha -> (BigDecimal) linha[1],
                        BigDecimal::add
                ));

        return new ResumoMensalDTO(
                totalReceitas,
                totalDespesas,
                saldoFinal,
                totalGastoPorCategoria
        );
    }
}