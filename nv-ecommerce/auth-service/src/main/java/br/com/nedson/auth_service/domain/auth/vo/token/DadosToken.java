package br.com.nedson.auth_service.domain.auth.vo.token;

import br.com.nedson.auth_service.domain.auth.vo.user.DetalharUsuario;

public record DadosToken(
        String tokenDeAcesso,
        DetalharUsuario usuario,
        String refreshToken
) {
}
