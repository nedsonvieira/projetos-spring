package br.com.nlw.events.repository;

import br.com.nlw.events.model.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    public Usuario findByEmail(String email);
}
