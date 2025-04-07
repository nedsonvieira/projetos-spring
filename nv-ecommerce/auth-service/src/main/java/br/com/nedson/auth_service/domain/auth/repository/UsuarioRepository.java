package br.com.nedson.auth_service.domain.auth.repository;

import br.com.nedson.auth_service.domain.auth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);
}
