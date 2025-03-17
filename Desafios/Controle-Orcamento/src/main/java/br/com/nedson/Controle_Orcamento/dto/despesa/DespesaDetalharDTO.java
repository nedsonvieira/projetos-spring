package br.com.nedson.Controle_Orcamento.dto.despesa;

import br.com.nedson.Controle_Orcamento.model.Categoria;
import br.com.nedson.Controle_Orcamento.model.Despesa;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public record DespesaDetalharDTO(

        String descricao,

        BigDecimal valor,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        String  data,

        Categoria categoria
) {
    public DespesaDetalharDTO(Despesa despesa){
        this(despesa.getDescricao(),
                despesa.getValor(),
                despesa.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                despesa.getCategoria()
        );
    }
}
