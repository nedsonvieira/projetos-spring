package br.com.nedson.Controle_Orcamento.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record EnviarEmailDTO(

        @NotBlank(message = "O email é obrigatório!")
        String email,

        @NotBlank(message = "O ano é obrigatório!")
        Integer ano,

        @NotBlank(message = "O mês é obrigatório!")
        Integer mes)
{
}
