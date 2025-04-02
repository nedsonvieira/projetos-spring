package br.com.nedson.api_med_voll.dto.consulta.validacoes.agendamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import br.com.nedson.api_med_voll.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarPacienteAtivo implements ValidadorAgendamentoConsulta{

    @Autowired
    private PacienteRepository repositorio;

    public void validar(DadosAgendarConsulta dados){
        var pacienteIsAtivo = repositorio.findAtivoById(dados.idPaciente());
        if (!pacienteIsAtivo){
            throw new ValidarDadosConsultaException("Consulta não pode ser agendada com paciente inativo!");
        }
    }

}
