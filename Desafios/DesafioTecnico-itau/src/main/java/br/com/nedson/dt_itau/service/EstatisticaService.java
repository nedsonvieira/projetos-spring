package br.com.nedson.dt_itau.service;

import br.com.nedson.dt_itau.model.Estatistica;
import br.com.nedson.dt_itau.model.Transacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstatisticaService {
    private final TransacaoService transacaoService;

    public Estatistica gerarEstatisticas(Integer intervalo){
        log.info("Gerando lista de estatísticas das transações que aconteceram nos últimos {} segundos...", intervalo);
        List<Transacao> transacoes = transacaoService.buscarTransacoesNoIntervalo(intervalo);

        if (transacoes.isEmpty()){
            return new Estatistica(0L, 0.0, 0.0, 0.0, 0.0);
        }

        DoubleSummaryStatistics stats = transacoes.stream()
                .mapToDouble(Transacao::valor)
                .summaryStatistics();
        log.info("Lista de estatísticas das transações que aconteceram nos últimos {} segundos gerada com sucesso!", intervalo);
        return new Estatistica(
                stats.getCount(),
                stats.getSum(),
                stats.getAverage(),
                stats.getMin(),
                stats.getMax()
        );
    }
}
