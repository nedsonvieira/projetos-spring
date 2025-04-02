package br.com.nedson.api_med_voll.dto.consulta.validacoes.agendamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;

public interface ValidadorAgendamentoConsulta {
    void validar(DadosAgendarConsulta dados);
}
