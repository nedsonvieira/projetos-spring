package br.com.nedson.auth_service.controller;

import br.com.nedson.auth_service.domain.auth.service.LoginService;
import br.com.nedson.auth_service.domain.auth.vo.token.DadosToken;
import br.com.nedson.auth_service.domain.auth.vo.user.DadosAutenticacao;
import br.com.nedson.auth_service.domain.auth.vo.user.DetalharUsuario;
import br.com.nedson.auth_service.infra.security.RefreshTokenService;
import br.com.nedson.auth_service.infra.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final LoginService loginService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<DadosToken> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {

        var usuario = loginService.realizarLogin(dados.email(), dados.senha());

        var token = tokenService.gerarToken(usuario);
        var refreshToken = refreshTokenService.gerarRefreshToken(usuario);

        return ResponseEntity.ok(new DadosToken(token, new DetalharUsuario(usuario), refreshToken));
    }
}