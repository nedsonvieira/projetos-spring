package br.com.nedson.auth_service.domain.vo.user;

import br.com.nedson.auth_service.domain.entity.Usuario;

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
