package br.com.nedson.auth_service.domain.repository;

import br.com.nedson.auth_service.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);
}
