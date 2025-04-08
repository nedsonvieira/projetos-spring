package br.com.nedson.auth_service.domain.auth.vo.user;

import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.vo.Role;

public record DadosUsuarioResponse(

        String nome,
        String email,
        Role role
) {
    public DadosUsuarioResponse(Usuario usuario){
        this(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole())
        ;
    }
}
