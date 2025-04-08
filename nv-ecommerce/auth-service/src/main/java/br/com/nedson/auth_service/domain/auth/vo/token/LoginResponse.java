package br.com.nedson.auth_service.domain.auth.vo.token;

import br.com.nedson.auth_service.domain.auth.vo.user.DadosUsuarioResponse;

public record LoginResponse(
        String tokenDeAcesso,
        DadosUsuarioResponse usuario,
        String refreshToken
) {
}
