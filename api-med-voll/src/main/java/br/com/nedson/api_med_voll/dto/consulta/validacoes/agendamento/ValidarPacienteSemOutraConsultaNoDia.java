package br.com.nedson.api_med_voll.dto.consulta.validacoes.agendamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import br.com.nedson.api_med_voll.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarPacienteSemOutraConsultaNoDia implements ValidadorAgendamentoConsulta{

    @Autowired
    private ConsultaRepository repositorio;

    public void validar(DadosAgendarConsulta dados){
        var horarioPrimeiraConsulta = dados.data().withHour(7);
        var horarioUltimaConsulta = dados.data().withHour(18);
        var pacientePossuiOutraConsultaNoMesmoDia = repositorio.existsByPacienteIdAndDataBetween(dados.idPaciente(), horarioPrimeiraConsulta, horarioUltimaConsulta);

        if (pacientePossuiOutraConsultaNoMesmoDia){
            throw new ValidarDadosConsultaException("Paciente já possui uma consulta agendada neste dia!");
        }
    }
}
