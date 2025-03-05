package br.com.nedson.AluraFlix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record DadosCadastrarVideo(

        @NotBlank(message = "O título é obrigatório!")
        @Size(max = 60, message = "O título deve ter no máximo 60 caracteres")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória!")
        String descricao,

        @NotBlank(message = "O URL é obrigatório!")
        @URL(message = "O formato do URL é inválido!")
        @Size(max = 100, message = "O URL deve ter no máximo 100 caracteres")
        String url
) {

}
