package br.com.nedson.auth_service.infra.security;

import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TokenService {

    private static final String ISSUER = "API_NV_E-COMMERCE";

    private final String secret;

    public TokenService(@Value("${app.security.jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getEmail())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Erro ao gerar Token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token JWT inválido ou expirado: " +tokenJWT);
        }
    }

    private Instant dataExpiracao() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.of("-03:00"));
        return zonedDateTime.plusMinutes(30).toInstant();
    }
}
