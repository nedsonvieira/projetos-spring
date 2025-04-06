package br.com.nedson.auth_service.controller;

import br.com.nedson.auth_service.domain.auth.service.RefreshTokenService;
import br.com.nedson.auth_service.domain.auth.vo.token.RefreshRequest;
import br.com.nedson.auth_service.domain.auth.entity.RefreshToken;
import br.com.nedson.auth_service.domain.auth.vo.token.TokenResponse;
import br.com.nedson.auth_service.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nv-ecommerce/auth")
@SecurityRequirement(name = "bearer-key")
@AllArgsConstructor
@Tag(name = "Autenticação", description = "Gerenciamento de tokens de autenticação, incluindo validação, renovação e revogação.")
public class AuthController {

    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @Operation(
            summary = "Validar token JWT",
            description = "Valida o token JWT recebido no cabeçalho 'Authorization' e retorna o subject (e-mail do usuário) associado ao token.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @GetMapping("/validar-token")
    public ResponseEntity<String> validarToken(@RequestHeader("Authorization") String token) {
        String tokenSemBearer = token.replace("Bearer ", ""); // Remove prefixo "Bearer"
        String subject = tokenService.getSubject(tokenSemBearer); // Valida e obtém o subject do token

        return ResponseEntity.ok("Token válido! Usuário: " + subject);
    }

    @Operation(
            summary = "Renovar tokens",
            description = "Valida o refresh token e renova o token JWT. É necessário estar autenticado e enviar um refresh token válido.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens renovados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na renovação - Refresh token inválido ou expirado"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado - Acesso negado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PostMapping("/renovar-token")
    public ResponseEntity<TokenResponse> renovarTokens(@RequestBody RefreshRequest request) {
        String refreshToken = request.refreshToken();

        // Valida o refresh token
        RefreshToken tokenValido = refreshTokenService.validarRefreshToken(refreshToken);

        // Revoga o refresh token antigo para evitar reutilizações
        refreshTokenService.revogarRefreshToken(refreshToken);

        // Gera novos tokens
        String newAccessToken = tokenService.gerarToken(tokenValido.getUsuario());
        String newRefreshToken = refreshTokenService.gerarRefreshToken(tokenValido.getUsuario());

        long accessExpiresIn = 30 * 60; // 15 minutos
        long refreshExpiresIn = 7 * 24 * 60 * 60; // 7 dias

        return ResponseEntity.ok(new TokenResponse(
                newAccessToken,
                newRefreshToken,
                accessExpiresIn,
                refreshExpiresIn
        ));
    }

    @Operation(
            summary = "Revogar refresh token",
            description = "Revoga um refresh token. É necessário estar autenticado e enviar um refresh token válido. Remove o refresh token da base de dados.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Refresh token revogado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na revogação - Refresh token inválido ou expirado"),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido - Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Refresh token não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PostMapping("/revogar-token")
    public ResponseEntity<String> revogarToken(@RequestBody RefreshRequest request) {
        refreshTokenService.revogarRefreshToken(request.refreshToken()); // Revoga o token
        return ResponseEntity.ok("Refresh token revogado com sucesso.");
    }
}