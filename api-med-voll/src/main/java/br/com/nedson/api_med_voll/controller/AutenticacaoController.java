package br.com.nedson.api_med_voll.controller;

import br.com.nedson.api_med_voll.dto.usuario.DadosAutenticacaoUsuario;
import br.com.nedson.api_med_voll.dto.usuario.Usuario;
import br.com.nedson.api_med_voll.infra.security.DadosTokenJWT;
import br.com.nedson.api_med_voll.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager gerenciador;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacaoUsuario dados){
        var authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = gerenciador.authenticate(authToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
