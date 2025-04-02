package br.com.nedson.api_med_voll.dto.consulta.validacoes.agendamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;

@Component
public class ValidarHorarioFuncionamento implements ValidadorAgendamentoConsulta{

    public void validar(DadosAgendarConsulta dados){
        var dataConsulta = dados.data();

        var isDomingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var isAberto = dataConsulta.getHour() < 7;
        var isFechado = dataConsulta.getHour() > 18;

        if (isDomingo || isAberto || isFechado){
            throw new ValidarDadosConsultaException("Consulta fora do horário de funcionamento!");
        }
    }
}
