package br.com.nedson.Controle_Orcamento.dto.receita;

import br.com.nedson.Controle_Orcamento.dto.ConverteData;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceitaAtualizarDTO(

        String descricao,

        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "A data deve estar no formato dd/MM/yyyy.")
        String  data
) {
        public LocalDate converteData(){
                return new ConverteData().converteData(data);
        }
}
