package br.com.nedson.auth_service.domain.auth.service;

import br.com.nedson.auth_service.domain.auth.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService implements UserDetailsService {

    private final UsuarioRepository repositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repositorio.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("EMAIL ou SENHA inválidos."));
    }
}
