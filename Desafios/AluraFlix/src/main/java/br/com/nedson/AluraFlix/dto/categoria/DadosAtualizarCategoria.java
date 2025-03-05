package br.com.nedson.AluraFlix.dto.categoria;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizarCategoria(

        @NotNull
        Long id,

        String titulo,

        @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "A cor deve estar no formato hexadecimal, ex: #FFFFFF")
        String cor
) {
        public boolean validarTitulo() {
                return titulo == null || titulo.trim().isEmpty();
        }
        public boolean validarCor() {
                return cor == null || cor.trim().isEmpty();
        }
}
