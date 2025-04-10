package br.com.nedson.auth_service.domain.vo.token;

import br.com.nedson.auth_service.domain.vo.user.DadosUsuarioResponse;

public record LoginResponse(
        String tokenDeAcesso,
        DadosUsuarioResponse usuario,
        String refreshToken
) {
}
