package br.com.nedson.Controle_Orcamento.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCadastroDTO(

        @NotBlank(message = "O nome é obrigatório!")
        String nome,

        @NotBlank(message = "O email é obrigatório!")
        @Email(message = "Email com formato inválido!")
        String email,

        @NotBlank(message = "A senha é obrigatória!")
        String senha
) {
}
