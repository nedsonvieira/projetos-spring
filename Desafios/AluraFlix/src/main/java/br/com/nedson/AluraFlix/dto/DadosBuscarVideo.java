package br.com.nedson.AluraFlix.dto;

import br.com.nedson.AluraFlix.model.Video;

public record DadosBuscarVideo(
        String titulo,
        String descricao,
        String url
) {
    public DadosBuscarVideo(Video video){
        this(
                video.getTitulo(),
                video.getDescricao(),
                video.getUrl()
        );
    }
}
