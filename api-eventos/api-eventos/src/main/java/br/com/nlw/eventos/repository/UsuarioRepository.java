package br.com.nlw.eventos.repository;

import br.com.nlw.eventos.model.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    public Usuario findByEmail(String email);
}
