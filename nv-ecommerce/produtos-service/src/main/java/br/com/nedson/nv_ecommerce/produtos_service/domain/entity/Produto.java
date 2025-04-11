package br.com.nedson.nv_ecommerce.produtos_service.domain.entity;

import br.com.nedson.nv_ecommerce.produtos_service.domain.Categoria;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoAtualizarRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.function.Consumer;

@Entity
@Table(name = "produtos")
@EqualsAndHashCode(of = "id")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;
    private String descricao;
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private Integer estoque;

    public void adicionarEstoque(int quantidade) {
        this.estoque = estoque + quantidade;
    }

    public void atualizar(ProdutoAtualizarRequestDto dto) {
        atualizarStr(dto.nome(), valor -> this.nome = valor);
        atualizarStr(dto.descricao(), valor -> this.descricao = valor);
        atualizarOutros(dto.preco(), valor -> this.preco = (BigDecimal) valor);
        atualizarOutros(dto.categoria(), valor -> this.categoria = (Categoria) valor);
    }

    private void atualizarStr(String novoValor, Consumer<String> setter) {
        if (novoValor != null && !novoValor.isBlank()) {
            setter.accept(novoValor);
        }
    }
    private void atualizarOutros(Object novoValor, Consumer<Object> setter) {
        if (novoValor != null) {
            setter.accept(novoValor);
        }
    }
}