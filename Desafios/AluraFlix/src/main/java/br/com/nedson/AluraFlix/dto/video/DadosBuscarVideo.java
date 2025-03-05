package br.com.nedson.AluraFlix.dto.video;

import br.com.nedson.AluraFlix.model.Video;

public record DadosBuscarVideo(
        Long id,
        Long idCategoria,
        String titulo,
        String descricao,
        String url
) {
    public DadosBuscarVideo(Video video){
        this(
                video.getId(),
                video.getCategoria().getId(),
                video.getTitulo(),
                video.getDescricao(),
                video.getUrl()
        );
    }
}
