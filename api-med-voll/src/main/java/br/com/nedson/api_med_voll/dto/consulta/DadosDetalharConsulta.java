package br.com.nedson.api_med_voll.dto.consulta;

import br.com.nedson.api_med_voll.model.Consulta;

import java.time.LocalDateTime;

public record DadosDetalharConsulta(Long id,
                                    Long idMedico,
                                    Long idPaciente,
                                    LocalDateTime data) {

    public DadosDetalharConsulta(Consulta consulta) {
        this(consulta.getId(),
                consulta.getMedico().getId(),
                consulta.getPaciente().getId(),
                consulta.getData());
    }
}
