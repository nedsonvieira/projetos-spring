package br.com.nedson.api_med_voll.dto.consulta.validacoes.agendamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import br.com.nedson.api_med_voll.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarMedicoAtivo implements ValidadorAgendamentoConsulta{

    @Autowired
    private MedicoRepository repositorio;

    public void validar(DadosAgendarConsulta dados){
        //escolha do medico opcional
        if(dados.idMedico() == null){
            return;
        }

        var medicoIsAtivo = repositorio.findAtivoById(dados.idMedico());
        if (!medicoIsAtivo){
            throw new ValidarDadosConsultaException("Consulta não pode ser agendada com médico inativo!");
        }
    }
}
