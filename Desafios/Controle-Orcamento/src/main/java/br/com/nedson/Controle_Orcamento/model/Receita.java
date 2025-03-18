package br.com.nedson.Controle_Orcamento.model;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receitas")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valor;

    private LocalDate data;


    public Receita(@Valid ReceitaCadastrarDTO dto){
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.data = dto.converteData();
    }

    public void atualizar(@Valid ReceitaAtualizarDTO dto){
        if (dto.descricao() != null && !dto.descricao().isBlank()) {
            this.descricao = dto.descricao();
        }
        if (dto.valor() != null) {
            this.valor = dto.valor();
        }
        if (dto.data() != null) {
            this.data = dto.converteData();
        }
    }
}
