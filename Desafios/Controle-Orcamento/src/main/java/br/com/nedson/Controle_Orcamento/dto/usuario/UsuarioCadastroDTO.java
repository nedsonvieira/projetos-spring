package br.com.nedson.Controle_Orcamento.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCadastroDTO(

        @NotBlank(message = "O nome é ")
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String senha
) {
}
