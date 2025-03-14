package br.com.nedson.Controle_Orcamento.dto.despesa;

import br.com.nedson.Controle_Orcamento.dto.ConverteData;
import br.com.nedson.Controle_Orcamento.model.Categoria;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;


public record DespesaCadastrarDTO(

        @NotBlank(message = "A descrição não pode ser nula ou vazia.")
        String descricao,

        @NotNull(message = "O valor não pode ser nulo.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "A data não pode ser nula.")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "A data deve estar no formato dd/MM/yyyy.")
        String data,

        Categoria categoria
) {
        public LocalDate converteData(){
                return new ConverteData().converteData(data);
        }
}
