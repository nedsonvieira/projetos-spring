package br.com.nedson.api_med_voll.dto.consulta;

import jakarta.validation.constraints.NotNull;

public record DadosCancelarConsulta(

        @NotNull
        Long idConsulta,

        @NotNull
        MotivoCancelamento motivo
) {
}
