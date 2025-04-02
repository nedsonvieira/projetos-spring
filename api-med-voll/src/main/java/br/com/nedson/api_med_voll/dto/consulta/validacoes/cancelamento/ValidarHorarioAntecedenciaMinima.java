package br.com.nedson.api_med_voll.dto.consulta.validacoes.cancelamento;

import br.com.nedson.api_med_voll.dto.consulta.DadosCancelarConsulta;
import br.com.nedson.api_med_voll.infra.excepiton.ValidarDadosConsultaException;
import br.com.nedson.api_med_voll.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidarHorarioAntecedenciaMinimaCancelamento")
public class ValidarHorarioAntecedenciaMinima implements ValidadorCancelamentoConsulta{

    @Autowired
    private ConsultaRepository repositorio;

    @Override
    public void validar(DadosCancelarConsulta dados) {
        var consulta = repositorio.getReferenceById(dados.idConsulta());
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

        if (diferencaEmHoras < 24) {
            throw new ValidarDadosConsultaException("Consulta somente pode ser cancelada com antecedência mínima de 24h!");
        }
    }
}
