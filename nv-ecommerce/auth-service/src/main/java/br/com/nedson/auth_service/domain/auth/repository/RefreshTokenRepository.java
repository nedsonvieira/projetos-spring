package br.com.nedson.auth_service.domain.auth.repository;

import br.com.nedson.auth_service.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    int deleteAllByExpiracaoBefore(Instant data);

    int deleteAllByUsuarioId(UUID usuarioId);

    void deleteByIdAndToken(UUID id, String token);
}
