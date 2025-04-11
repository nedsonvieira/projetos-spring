package br.com.nedson.nv_ecommerce.produtos_service.domain.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record EventoProduto (
        String tipoEvento,

        ProdutoResponseDto dados,

        Instant dataHora
){
}
