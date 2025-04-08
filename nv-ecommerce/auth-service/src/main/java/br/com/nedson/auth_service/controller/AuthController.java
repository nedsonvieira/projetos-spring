package br.com.nedson.auth_service.controller;

import br.com.nedson.auth_service.domain.auth.entity.RefreshToken;
import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.vo.token.RefreshTokenRequest;
import br.com.nedson.auth_service.domain.auth.vo.token.RenovarRefreshTokenResponse;
import br.com.nedson.auth_service.domain.auth.vo.token.TokenIntrospectResponse;
import br.com.nedson.auth_service.domain.auth.vo.token.TokenRequest;
import br.com.nedson.auth_service.infra.security.RefreshTokenService;
import br.com.nedson.auth_service.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            description = "Valida o token JWT e retorna dados do usuário associado ao token. Deve ter o perfil de ADMIN",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "403", description = "Acesso Negado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @GetMapping("/introspect")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TokenIntrospectResponse> validarToken(@RequestBody TokenRequest tokenRequest) {
        Usuario usuario = tokenService.obterUsuario(tokenRequest.token());

        return ResponseEntity.ok(new TokenIntrospectResponse(
                tokenService.isExpirado(tokenRequest.token()),
                usuario.getId(),
                usuario.getEmail(),
                usuario.getRole()
        ));
    }

    @Operation(
            summary = "Renovar tokens",
            description = "Valida o refresh token e renova o token JWT. É necessário estar autenticado e enviar um refresh token válido.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens renovados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na renovação - Refresh token inválido"),
            @ApiResponse(responseCode = "401", description = "Token expirado - Não Autorizado"),
            @ApiResponse(responseCode = "403", description = "Sem autenticação - Acesso Negado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PostMapping("/renovar-token")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'CLIENTE')")
    public ResponseEntity<RenovarRefreshTokenResponse> renovarTokens(@AuthenticationPrincipal Usuario usuario, @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        // Valida o refresh token
        RefreshToken tokenValido = refreshTokenService.validarRefreshToken(refreshToken);

        // Revoga o refresh token antigo para evitar reutilizações
        refreshTokenService.revogarRefreshToken(usuario.getId(), refreshToken);

        // Gera novos tokens
        String novoAccessToken = tokenService.gerarToken(tokenValido.getUsuario());
        String novoRefreshToken = refreshTokenService.gerarRefreshToken(tokenValido.getUsuario());

        long accessExpiraEm = 30 * 60; // 30 minutos
        long refreshExpiraEm = 7 * 24 * 60 * 60; // 7 dias

        return ResponseEntity.ok(new RenovarRefreshTokenResponse(
                novoAccessToken,
                novoRefreshToken,
                accessExpiraEm,
                refreshExpiraEm
        ));
    }

    @Operation(
            summary = "Revogar refresh token",
            description = "Revoga um refresh token. É necessário estar autenticado e enviar um refresh token válido.",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Refresh token revogado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na revogação - Refresh token inválido ou expirado"),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido - Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Refresh token não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @DeleteMapping("/revogar-token")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'CLIENTE')")
    public ResponseEntity<String> revogarTodosToken(@AuthenticationPrincipal Usuario usuario, @RequestBody RefreshTokenRequest request) {
        refreshTokenService.revogarRefreshToken(usuario.getId(), request.refreshToken());
        return ResponseEntity.ok("Refresh token revogado com sucesso.");
    }

    @Operation(
            summary = "Revogar todos refresh token",
            description = "Revoga todos os refresh tokens de um usuário. É necessário estar autenticado e enviar um refresh token válido. Logout global",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Refresh tokens revogados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na revogação - Token inválido ou expirado"),
            @ApiResponse(responseCode = "401", description = "Token inválido - Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Refresh tokens não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @DeleteMapping("/revogar-todos")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'CLIENTE')")
    public ResponseEntity<String> revogarToken(@AuthenticationPrincipal Usuario usuario) {
        int tokensRevogados = refreshTokenService.revogarTodosRefreshToken(usuario.getId());

        return ResponseEntity.ok("Total de refresh tokens revogados: " + tokensRevogados);
    }
}