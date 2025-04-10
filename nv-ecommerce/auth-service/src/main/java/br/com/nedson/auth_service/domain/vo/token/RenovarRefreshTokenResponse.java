package br.com.nedson.auth_service.domain.vo.token;

public record RenovarRefreshTokenResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiraEm,
        long refreshTokenExpiraEm
) {
}
