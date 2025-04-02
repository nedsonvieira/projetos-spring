package br.com.nedson.api_med_voll.dto.consulta.validacoes.cancelamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosCancelarConsulta;

public interface ValidadorCancelamentoConsulta {
    void validar(DadosCancelarConsulta dados);
}
