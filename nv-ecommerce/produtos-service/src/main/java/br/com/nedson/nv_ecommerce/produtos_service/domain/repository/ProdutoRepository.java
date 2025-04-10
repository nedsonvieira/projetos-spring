package br.com.nedson.nv_ecommerce.produtos_service.domain.repository;

import br.com.nedson.nv_ecommerce.produtos_service.domain.Categoria;
import br.com.nedson.nv_ecommerce.produtos_service.domain.entity.Produto;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, String> {
    @Nonnull
    Optional<Produto> findById(@Nonnull String id);

    boolean existsByNome(String nome);

    boolean existsByCategoria(Categoria categoria);

    Page<Produto> findAllByCategoria(Categoria categoria, Pageable pageable);
}
