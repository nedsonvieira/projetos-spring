package br.com.nedson.Controle_Orcamento.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(

        @NotBlank
        String email,

        @NotBlank
        String senha) {
}
