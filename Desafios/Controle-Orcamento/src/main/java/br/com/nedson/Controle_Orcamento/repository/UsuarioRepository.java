package br.com.nedson.Controle_Orcamento.repository;

import br.com.nedson.Controle_Orcamento.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);

    Boolean existsByEmail(String email);
}
