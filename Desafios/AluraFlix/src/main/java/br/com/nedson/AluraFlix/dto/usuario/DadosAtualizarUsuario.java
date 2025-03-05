package br.com.nedson.AluraFlix.dto.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizarUsuario(
        @NotNull
        Long id,

        String login,

        String senha,

        Role role
) {
}
