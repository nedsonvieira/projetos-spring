package br.com.nedson.Controle_Orcamento.dto.receita;

import br.com.nedson.Controle_Orcamento.infra.exception.ValidarDataException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record ReceitaAtualizarDTO(

        @NotNull
        Long id,

        String descricao,

        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero!")
        BigDecimal valor,

        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "A data deve estar no formato dd/MM/yyyy!")
        String  data
) {
        public LocalDate converteData(){
                var data = LocalDate.parse(this.data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (data.isAfter(LocalDate.now())){
                        throw new ValidarDataException("A data não pode estar no fututo!");
                }
                return data;
        }
}
