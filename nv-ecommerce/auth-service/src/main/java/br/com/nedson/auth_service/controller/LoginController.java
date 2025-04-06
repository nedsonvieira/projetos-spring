package br.com.nedson.auth_service.controller;

import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.service.RefreshTokenService;
import br.com.nedson.auth_service.domain.auth.service.UsuarioService;
import br.com.nedson.auth_service.domain.auth.vo.user.DadosAutenticacao;
import br.com.nedson.auth_service.domain.auth.vo.token.DadosToken;
import br.com.nedson.auth_service.domain.auth.vo.user.DetalharUsuario;
import br.com.nedson.auth_service.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nv-ecommerce/auth")
@AllArgsConstructor
@Tag(name = "Login", description = "Endpoints para autenticação e geração de tokens")
public class LoginController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;
    private final RefreshTokenService refreshTokenService;


    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza o login do usuário, gerando um token de acesso (JWT) e um refresh token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na validação dos dados de autenticação"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas - Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Token inválido ou expirado - Acesso negado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<DadosToken> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var token = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        var usuario = usuarioService.findByEmail(dados.email());
        var refreshToken = refreshTokenService.gerarRefreshToken(usuario);

        return ResponseEntity.ok(new DadosToken(token, new DetalharUsuario(usuario), refreshToken));
    }
}