package br.com.nedson.AluraFlix.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastrarUsuario(

        @NotBlank(message = "O login é obrigatório!")
        String login,

        @NotBlank(message = "A senha é obrigatória!")
        String senha,

        @NotNull(message = "O perfil é obrigatório!")
        Role role
) {
}
