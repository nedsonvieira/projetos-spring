package br.com.nedson.auth_service.domain.auth.service;

import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final UsuarioRepository repositorio;

    private final PasswordEncoder encoder;

    public Usuario realizarLogin(String email, String senha) {

        var usuario = repositorio.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("EMAIL ou SENHA inválidos."));

        if (encoder.matches(senha, usuario.getSenha())) {
            return usuario;
        } else {
            throw new BadCredentialsException("EMAIL ou SENHA inválidos.");
        }
    }
}
