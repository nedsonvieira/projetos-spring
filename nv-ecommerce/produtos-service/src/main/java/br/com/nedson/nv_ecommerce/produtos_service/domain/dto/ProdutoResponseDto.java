package br.com.nedson.nv_ecommerce.produtos_service.domain.dto;

import br.com.nedson.nv_ecommerce.produtos_service.domain.Categoria;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProdutoResponseDto(

        String id,

        String nome,

        String descricao,

        BigDecimal preco,

        Categoria categoria,

        Integer estoque
) {
}
