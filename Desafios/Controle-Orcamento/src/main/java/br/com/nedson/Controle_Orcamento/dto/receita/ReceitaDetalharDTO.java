package br.com.nedson.Controle_Orcamento.dto.receita;

import br.com.nedson.Controle_Orcamento.model.Receita;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public record ReceitaDetalharDTO(

        String descricao,

        BigDecimal valor,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        String  data
) {
    public ReceitaDetalharDTO(Receita receita){
        this(receita.getDescricao(),
                receita.getValor(),
                receita.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }
}
