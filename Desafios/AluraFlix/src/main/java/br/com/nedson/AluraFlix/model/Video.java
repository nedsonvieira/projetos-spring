package br.com.nedson.AluraFlix.model;

import br.com.nedson.AluraFlix.dto.video.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosCadastrarVideo;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Video")
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private String url;

    private Boolean ativo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Video(DadosCadastrarVideo dados, Categoria categoria){
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
        this.url = dados.url();
        this.ativo = true;
        this.categoria = categoria;
    }

//    public List<Video> converterLista(List<DadosCadastrarVideo> listaDados) {
//        return listaDados.stream()
//                .map(v-> new Video(null, v.titulo(), v.descricao(), v.url(), true))
//                .toList();
//    }

    public void atualizar(@Valid DadosAtualizarVideo dados){
        if (!dados.validarTitulo()){
            this.titulo = dados.titulo();
        }
        if (!dados.validarDescricao()){
            this.descricao = dados.descricao();
        }
        if (!dados.validarUrl()){
            this.url = dados.url();
        }
    }

    public void inativar(){
        this.ativo = false;
    }
}
