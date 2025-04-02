package br.com.nedson.api_med_voll.dto.consulta;

import br.com.nedson.api_med_voll.dto.medico.Especialidade;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendarConsulta(

        Long idMedico,

        @NotNull
        Long idPaciente,

        @NotNull
        @Future
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime data,

        Especialidade especialidade
) {
}
