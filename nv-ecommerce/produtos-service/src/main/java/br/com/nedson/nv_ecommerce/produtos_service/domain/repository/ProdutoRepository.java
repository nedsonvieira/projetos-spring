package br.com.nedson.nv_ecommerce.produtos_service.domain.repository;

import br.com.nedson.nv_ecommerce.produtos_service.domain.Categoria;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoResponseDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    boolean existsByNome(String nome);

    boolean existsByCategoria(Categoria categoria);

    Page<Produto> findAllByCategoria(Categoria categoria, Pageable pageable);
}
