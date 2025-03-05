package br.com.nedson.AluraFlix.dto.usuario;

import br.com.nedson.AluraFlix.model.Usuario;

public record DadosBuscarUsuario(
        String login,
        String senha
) {
    public DadosBuscarUsuario(Usuario usuario){
        this(
                usuario.getLogin(),
                usuario.getSenha()
        );
    }
}
