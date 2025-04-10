package br.com.nedson.auth_service.controller;

import br.com.nedson.auth_service.domain.entity.Usuario;
import br.com.nedson.auth_service.domain.vo.token.LoginResponse;
import br.com.nedson.auth_service.domain.vo.user.DadosUsuarioResponse;
import br.com.nedson.auth_service.domain.vo.user.LoginRequest;
import br.com.nedson.auth_service.infra.security.RefreshTokenService;
import br.com.nedson.auth_service.infra.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nv-ecommerce/auth")
@AllArgsConstructor
@Tag(name = "Login", description = "Endpoints para autenticação e geração de tokens")
public class LoginController {

    private final TokenService tokenService;
    private final AuthenticationManager manager;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> efetuarLogin(@RequestBody @Valid LoginRequest dados) {

        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(dados.email(), dados.senha())
        );
        Usuario usuario = (Usuario) authentication.getPrincipal();

        var token = tokenService.gerarToken(usuario);
        var refreshToken = refreshTokenService.gerarRefreshToken(usuario);

        return ResponseEntity.ok(new LoginResponse(token, new DadosUsuarioResponse(usuario), refreshToken));
    }
}