package br.com.nedson.AluraFlix.dto.video;

import br.com.nedson.AluraFlix.model.Video;

public record DadosDetalharVideo(
        Long id,
        String titulo,
        String descricao,
        String url,
        Long categoriaId
) {
    public DadosDetalharVideo(Video video){
        this(
                video.getId(),
                video.getTitulo(),
                video.getDescricao(),
                video.getUrl(),
                video.getCategoria().getId()
        );
    }
}
