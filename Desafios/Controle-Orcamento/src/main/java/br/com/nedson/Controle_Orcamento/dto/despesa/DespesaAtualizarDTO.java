package br.com.nedson.Controle_Orcamento.dto.despesa;

import br.com.nedson.Controle_Orcamento.infra.exception.ValidarDataException;
import br.com.nedson.Controle_Orcamento.model.Categoria;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record DespesaAtualizarDTO(

        @NotNull
        Long id,

        String descricao,

        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero!")
        BigDecimal valor,

        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "A data deve estar no formato dd/MM/yyyy!")
        String  data,

        Categoria categoria
) {
        public LocalDate converteData(){
                var data = LocalDate.parse(this.data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (data.isAfter(LocalDate.now())){
                        throw new ValidarDataException("A data não pode estar no fututo!");
                }
                return data;
        }
}
