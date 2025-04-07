package br.com.nedson.auth_service.infra.security;

import br.com.nedson.auth_service.domain.auth.entity.RefreshToken;
import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import br.com.nedson.auth_service.domain.auth.repository.RefreshTokenRepository;
import br.com.nedson.auth_service.infra.exception.TokenExpiradoException;
import br.com.nedson.auth_service.infra.exception.TokenInvalidoException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repositorio;

    @Transactional
    public String gerarRefreshToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken(
                token,
                usuario,
                Instant.now().plusSeconds(60 * 60 * 24 * 7)
        );
        repositorio.save(refreshToken);

        return token;
    }

    public RefreshToken validarRefreshToken(String token) {
        RefreshToken refreshToken = repositorio.findByToken(token)
                .orElseThrow(() -> new TokenInvalidoException("Erro ao validar: Refresh token inválido ou não encontrado!"));

        if (refreshToken.getExpiracao().isBefore(Instant.now())) {
            throw new TokenExpiradoException("Refresh token expirado");
        }
        return refreshToken;
    }

    @Transactional
    public void revogarRefreshToken(String token) {
        repositorio.findByToken(token)
                .ifPresentOrElse(
                        repositorio::delete,
                        () -> { throw new TokenInvalidoException("Erro ao revogar: Refresh token inválido ou não encontrado!"); }
                );
    }
}
