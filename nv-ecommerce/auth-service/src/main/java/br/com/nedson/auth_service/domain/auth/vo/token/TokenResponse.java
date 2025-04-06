package br.com.nedson.auth_service.domain.auth.vo.token;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiraEm,
        long refreshTokenExpiraEm
) {
}
