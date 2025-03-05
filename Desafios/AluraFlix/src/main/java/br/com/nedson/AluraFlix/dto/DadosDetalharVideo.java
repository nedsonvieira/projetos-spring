package br.com.nedson.AluraFlix.dto;

import br.com.nedson.AluraFlix.model.Video;

public record DadosDetalharVideo(
        Long id,
        String titulo,
        String descricao,
        String url
) {
    public DadosDetalharVideo(Video video){
        this(
                video.getId(),
                video.getTitulo(),
                video.getDescricao(),
                video.getUrl()
        );
    }
}
