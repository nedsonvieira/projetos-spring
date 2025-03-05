package br.com.nedson.AluraFlix.dto.categoria;

import br.com.nedson.AluraFlix.model.Categoria;

public record DadosDetalharCategoria(
        Long id,
        String  titulo,
        String cor
) {
    public DadosDetalharCategoria(Categoria categoria){
        this(
                categoria.getId(),
                categoria.getTitulo(),
                categoria.getCor()
        );
    }
}
