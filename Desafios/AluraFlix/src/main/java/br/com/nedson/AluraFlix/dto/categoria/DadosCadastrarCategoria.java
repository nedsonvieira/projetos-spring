package br.com.nedson.AluraFlix.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastrarCategoria(

    @NotBlank(message = "O título é obrigatório!")
    String titulo,

    @NotBlank(message = "A cor é obrigatória!")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "A cor deve estar no formato hexadecimal, ex: #FFFFFF")
    String cor
){
}
