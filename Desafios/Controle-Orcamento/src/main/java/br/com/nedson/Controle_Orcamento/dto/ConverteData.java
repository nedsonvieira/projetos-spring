package br.com.nedson.Controle_Orcamento.dto;

import br.com.nedson.Controle_Orcamento.infra.exception.ValidarDataException;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConverteData {


    public LocalDate converteData(String  data) {
        var dataConvertida = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return validarData(dataConvertida);
    }

    @PastOrPresent(message = "A data não pode estar no futuro")
    public LocalDate validarData(LocalDate  data) {
        if (data.isAfter(LocalDate.now())) {
            throw new ValidarDataException("A data não pode estar no futuro");
        }
        return data;
    }
}
