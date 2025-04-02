package br.com.nedson.api_med_voll.dto.consulta.validacoes.agendamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import br.com.nedson.api_med_voll.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarMedicoConsultaNoMesmoHorario implements ValidadorAgendamentoConsulta{

    @Autowired
    private ConsultaRepository repositorio;

    public void validar(DadosAgendarConsulta dados){
        var medicoComConsultaMesmoHorario = repositorio.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(dados.idMedico(), dados.data());

        if (medicoComConsultaMesmoHorario){
            throw new ValidarDadosConsultaException("Médico indisponível para este horário!");
        }
    }
}
