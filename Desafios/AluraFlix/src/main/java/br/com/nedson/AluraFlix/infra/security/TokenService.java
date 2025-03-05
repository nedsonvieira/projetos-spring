package br.com.nedson.AluraFlix.infra.security;

import br.com.nedson.AluraFlix.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.senha}")
    private String senha;

    public String gerarToken(Usuario usuario){
        try {
            var alg = Algorithm.HMAC256(senha);

            return JWT.create()
                    .withIssuer("API Alura Flix")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(alg);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar o token jwt", e);
        }
    }

    public String getSubject(String tokenJWT){
        try {
            var alg = Algorithm.HMAC256(senha);

            return JWT.require(alg)
                    .withIssuer("API Alura Flix")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
