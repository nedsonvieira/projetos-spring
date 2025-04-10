package br.com.nedson.auth_service.infra.security;

import br.com.nedson.auth_service.domain.entity.RefreshToken;
import br.com.nedson.auth_service.domain.entity.Usuario;
import br.com.nedson.auth_service.domain.repository.RefreshTokenRepository;
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
        repositorio.deleteAllByUsuarioId(usuario.getId());

        RefreshToken refreshToken = new RefreshToken(usuario);
        repositorio.save(refreshToken);

        return refreshToken.getToken();
    }

    public RefreshToken validarRefreshToken(String token) {
        RefreshToken refreshToken = repositorio.findByToken(token)
                .orElseThrow(() -> new TokenInvalidoException("Erro ao validar: Refresh token inválido ou não encontrado!"));

        if (refreshToken.getExpiracao().isBefore(Instant.now())) {
            throw new TokenExpiradoException("Refresh token expirado!");
        }
        return refreshToken;
    }

    @Transactional
    public void revogarRefreshToken(UUID id, String token) {
        repositorio.deleteByIdAndToken(id, token);
    }

    @Transactional
    public int revogarTodosRefreshToken(UUID id) {
        return repositorio.deleteAllByUsuarioId(id);
    }
}
