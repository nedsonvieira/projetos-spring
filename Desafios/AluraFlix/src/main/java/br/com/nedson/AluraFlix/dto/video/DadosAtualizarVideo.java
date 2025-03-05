package br.com.nedson.AluraFlix.dto.video;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record DadosAtualizarVideo(

        @NotNull
        Long id,

        String titulo,

        String descricao,

        @URL(message = "O formato do URL é inválido!")
        String url

) {
        public boolean validarTitulo() {
                return titulo == null || titulo.trim().isEmpty();
        }
        public boolean validarDescricao() {
                return descricao == null || descricao.trim().isEmpty();
        }
        public boolean validarUrl() {
                return url == null || url.trim().isEmpty();
        }
}
