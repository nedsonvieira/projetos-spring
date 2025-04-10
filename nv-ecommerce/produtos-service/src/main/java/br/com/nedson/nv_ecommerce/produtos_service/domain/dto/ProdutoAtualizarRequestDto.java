package br.com.nedson.nv_ecommerce.produtos_service.domain.dto;

import br.com.nedson.nv_ecommerce.produtos_service.domain.Categoria;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequestDto(

        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracteres.")
        String nome,

        @NotBlank(message = "A descrição é obrigatória.")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        BigDecimal preco,

        @NotNull(message = "A categoria é obrigatória.")
        Categoria categoria,

        @NotNull(message = "O estoque é obrigatório.")
        @Min(value = 0, message = "O estoque deve ser no mínimo 0.")
        Integer estoque
) {
}
