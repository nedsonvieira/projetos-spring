package br.com.nedson.auth_service.infra.security;

import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.repository.UsuarioRepository;
import br.com.nedson.auth_service.infra.exception.TokenInvalidoException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class TokenService {

    private static final String ISSUER = "API_NV_E-COMMERCE";

    private final String secret;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    public TokenService(@Value("${app.security.jwt.secret}") String secret, UsuarioRepository usuarioRepository) {
        this.secret = secret;
        this.usuarioRepository = usuarioRepository;
    }

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getId().toString())
                    .withClaim("email", usuario.getEmail())
                    .withClaim("role", usuario.getRole().name())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);

        } catch (JWTCreationException ex) {
            throw new JWTCreationException("Erro ao gerar Token JWT", ex);
        }
    }

    public Usuario obterUsuario(String token) {
        return usuarioRepository.findById(getSubject(token))
                .orElseThrow(() -> new TokenInvalidoException("Token inválido ou expirado!"));
    }

    public UUID getSubject(String token) {
        return UUID.fromString(validarToken(token).getSubject());
    }

    public boolean isExpirado(String token){
        return validarToken(token).getExpiresAtAsInstant().isAfter((Instant.now()));
    }

    private DecodedJWT validarToken(String token) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
        } catch (RuntimeException ex) {
            throw new TokenInvalidoException("Token inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.of("-03:00"));
        return zonedDateTime.plusMinutes(30).toInstant();
    }
}