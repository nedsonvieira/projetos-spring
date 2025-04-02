package br.com.nedson.api_med_voll.dto.consulta.validacoes.cancelamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosCancelarConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import br.com.nedson.api_med_voll.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarConsultaJaFoiCancelada implements ValidadorCancelamentoConsulta{

    @Autowired
    private ConsultaRepository repositorio;

    @Override
    public void validar(DadosCancelarConsulta dados) {
        var consultaJaCancelada = repositorio.existsByIdAndMotivoCancelamentoIsNotNull(dados.idConsulta());

        if (consultaJaCancelada){
            throw new ValidarDadosConsultaException("Esta consulta já foi cancelada!");
        }
    }
}
