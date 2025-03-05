package br.com.nedson.AluraFlix.dto.usuario;

import br.com.nedson.AluraFlix.model.Usuario;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record DadosDetalharUsuario(

        @NotNull
        Long id,

        String login,

        Role role
) {
    public DadosDetalharUsuario(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getRole()
        );
    }
}
