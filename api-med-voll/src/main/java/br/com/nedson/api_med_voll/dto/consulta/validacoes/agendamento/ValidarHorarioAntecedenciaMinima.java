package br.com.nedson.api_med_voll.dto.consulta.validacoes.agendamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidarHorarioAntecedenciaMinimaAgendamento")
public class ValidarHorarioAntecedenciaMinima implements ValidadorAgendamentoConsulta{

    public void validar(DadosAgendarConsulta dados){
        var dataConsulta = dados.data();

        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if (diferencaEmMinutos < 30){
            throw new ValidarDadosConsultaException("Consulta deve ser agendada com antecedência mínima de 30 minutos!");
        }
    }
}
