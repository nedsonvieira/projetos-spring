package br.com.nedson.api_omdb_consumer.dto;

import br.com.nedson.api_omdb_consumer.model.Categoria;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Categoria genero,
                       String atores,
                       String poster,
                       String sinopse){
}
