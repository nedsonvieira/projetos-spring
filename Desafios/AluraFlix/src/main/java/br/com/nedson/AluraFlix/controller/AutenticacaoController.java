package br.com.nedson.AluraFlix.controller;

import br.com.nedson.AluraFlix.dto.usuario.DadosAutenticacaoUsuario;
import br.com.nedson.AluraFlix.infra.security.DadosTokenJWT;
import br.com.nedson.AluraFlix.infra.security.TokenService;
import br.com.nedson.AluraFlix.model.Usuario;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class AutenticacaoController {

    private AuthenticationManager gerenciador;

    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacaoUsuario dados){
        var authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = gerenciador.authenticate(authToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}