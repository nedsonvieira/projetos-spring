package br.com.nedson.Controle_Orcamento.model;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "despesas")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valor;

    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    public Despesa(@Valid DespesaCadastrarDTO dto){
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data = dto.converteData();
        if (dto.categoria() == null){
            this.categoria = Categoria.OUTRAS;
        }else {
            this.categoria = dto.categoria();
        }
    }

    public void atualizar(@Valid DespesaAtualizarDTO dto){
        if (dto.descricao() != null && !dto.descricao().isBlank()) {
            this.descricao = dto.descricao();
        }
        if (dto.valor() != null) {
            this.valor = dto.valor();
        }
        if (dto.data() != null) {
            this.data = dto.converteData();
        }
        if (dto.categoria() != null) {
            this.categoria = dto.categoria();
        }
    }
}
