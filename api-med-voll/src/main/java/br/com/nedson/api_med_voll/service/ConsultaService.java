package br.com.nedson.api_med_voll.service;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.dto.consulta.DadosCancelarConsulta;
import br.com.nedson.api_med_voll.dto.consulta.DadosDetalharConsulta;
import br.com.nedson.api_med_voll.dto.consulta.validacoes.agendamento.ValidadorAgendamentoConsulta;
import br.com.nedson.api_med_voll.dto.consulta.validacoes.cancelamento.ValidadorCancelamentoConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import br.com.nedson.api_med_voll.model.Consulta;
import br.com.nedson.api_med_voll.model.Medico;
import br.com.nedson.api_med_voll.repository.ConsultaRepository;
import br.com.nedson.api_med_voll.repository.MedicoRepository;
import br.com.nedson.api_med_voll.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository repositorioConsulta;

    @Autowired
    private MedicoRepository repositorioMedico;

    @Autowired
    private PacienteRepository repositorioPaciente;

    @Autowired
    private List<ValidadorAgendamentoConsulta> validadoresAgendamento;

    @Autowired
    private List<ValidadorCancelamentoConsulta> validadoresCancelamento;

    public DadosDetalharConsulta agendar(DadosAgendarConsulta dados){
        if (dados.idMedico() != null && !repositorioMedico.existsById(dados.idMedico())){
            throw new ValidarDadosConsultaException("O ID do médico informado não existe!");
        }
        if (!repositorioPaciente.existsById(dados.idPaciente())){
            throw new ValidarDadosConsultaException("O ID do paciente informado não existe!");
        }

        validadoresAgendamento.forEach(v -> v.validar(dados));

        var medico = escolherMedico(dados);
        var paciente = repositorioPaciente.getReferenceById(dados.idPaciente());

        if (medico == null){
            throw new ValidarDadosConsultaException("Não existe médico disponível nessa data para a especialidade escolhida!");
        }

        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        repositorioConsulta.save(consulta);

        return new DadosDetalharConsulta(consulta);
    }

    public void cancelar(DadosCancelarConsulta dados) {
        if (!repositorioConsulta.existsById(dados.idConsulta())) {
            throw new ValidarDadosConsultaException("Id da consulta informada não existe!");
        }
        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = repositorioConsulta.getReferenceById(dados.idConsulta());

        if (consulta.getMotivoCancelamento() != null){
            throw new IllegalStateException("A consulta já foi cancelada anteriormente");
        }
        consulta.cancelar(dados.motivo());
    }

    private Medico escolherMedico(DadosAgendarConsulta dados) {
        if (dados.idMedico() != null){
            return repositorioMedico.getReferenceById(dados.idMedico());
        }
        if (dados.especialidade() == null){
            throw new ValidarDadosConsultaException("Especialidade é obrigatória quando o médico não for escolhido!");
        }
        return repositorioMedico.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }
}
