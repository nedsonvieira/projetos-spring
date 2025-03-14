package br.com.nedson.Controle_Orcamento.dto.despesa;

import br.com.nedson.Controle_Orcamento.dto.ConverteData;
import br.com.nedson.Controle_Orcamento.model.Categoria;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaAtualizarDTO(

        @NotNull
        Long id,

        String descricao,

        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @PastOrPresent(message = "A data deve ser no passado ou presente.")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "A data deve estar no formato dd/MM/yyyy.")
        String  data,

        Categoria categoria
) {
        public LocalDate converteData(){
                return new ConverteData().converteData(data);
        }
}
