package br.com.nedson.AluraFlix.repository;

import br.com.nedson.AluraFlix.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Page<Categoria> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
           SELECT c.ativo FROM Categoria c
           WHERE c.id = :id""")
    Boolean findAtivoById(Long id);

    Boolean existsByTitulo(String titulo);

    Boolean existsByIdAndAtivoTrue(long l);

    Optional<Categoria> findByIdAndAtivoTrue(Long id);

    Optional<Categoria> findByTitulo(String titulo);
}
