package br.com.nedson.AluraFlix.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record DadosAtualizarVideo(

        Long id,

        @NotBlank(message = "O título é obrigatório!")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória!")
        String descricao,

        @NotBlank(message = "O URL é obrigatório!")
        @URL(message = "O formato do URL é inválido!")
        String url

) {
}
