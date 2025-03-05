package br.com.nedson.AluraFlix.model;

import br.com.nedson.AluraFlix.dto.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.DadosCadastrarVideo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    public Video(DadosCadastrarVideo dados){
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
        this.url = dados.url();
        this.ativo = true;
    }

//    public List<Video> converterLista(List<DadosCadastrarVideo> listaDados) {
//        return listaDados.stream()
//                .map(v-> new Video(null, v.titulo(), v.descricao(), v.url(), true))
//                .toList();
//    }

    public void atualizar(DadosAtualizarVideo dados){
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
        this.url = dados.url();
    }

    public void inativar(){
        this.ativo = false;
    }

}
