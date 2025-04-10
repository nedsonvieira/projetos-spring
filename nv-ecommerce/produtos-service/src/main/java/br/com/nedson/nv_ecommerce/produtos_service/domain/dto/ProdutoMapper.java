package br.com.nedson.nv_ecommerce.produtos_service.domain.dto;

import br.com.nedson.nv_ecommerce.produtos_service.domain.entity.Produto;

public class ProdutoMapper {

    public static Produto toProduto(ProdutoCadastrarRequestDto dto){
        return Produto.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .categoria(dto.categoria())
                .preco(dto.preco())
                .estoque(dto.estoque())
                .build();
    }

    public static ProdutoResponseDto toResponse(Produto produto) {
        return ProdutoResponseDto.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .categoria(produto.getCategoria())
                .preco(produto.getPreco())
                .estoque(produto.getEstoque())
                .build();
    }
}