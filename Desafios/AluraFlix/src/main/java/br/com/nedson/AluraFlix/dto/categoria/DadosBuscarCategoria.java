package br.com.nedson.AluraFlix.dto.categoria;

import br.com.nedson.AluraFlix.model.Categoria;

public record DadosBuscarCategoria(
        String titulo,
        String cor
) {
    public DadosBuscarCategoria(Categoria categoria){
        this(
                categoria.getTitulo(),
                categoria.getCor()
        );
    }
}
