package br.com.nedson.auth_service.domain.auth.vo.token;

public record RenovarRefreshTokenResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiraEm,
        long refreshTokenExpiraEm
) {
}
