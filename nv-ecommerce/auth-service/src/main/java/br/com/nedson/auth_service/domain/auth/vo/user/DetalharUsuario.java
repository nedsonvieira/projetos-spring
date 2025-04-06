package br.com.nedson.auth_service.domain.auth.vo.user;

import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.vo.Role;

public record DetalharUsuario(

        String nome,
        String email,
        Role role
) {
    public DetalharUsuario(Usuario usuario){
        this(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole())
        ;
    }
}
