package br.com.nedson.AluraFlix.model;

import br.com.nedson.AluraFlix.dto.categoria.DadosAtualizarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosCadastrarCategoria;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Categoria")
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private String cor;

    private Boolean ativo;

    public Categoria(DadosCadastrarCategoria dados){
        this.titulo = dados.titulo();
        this.cor = dados.cor();
        this.ativo = true;
    }

    public void atualizar(@Valid DadosAtualizarCategoria dados){
        if (!dados.validarTitulo()){
            this.titulo = dados.titulo();
        }
        if (!dados.validarCor()){
            this.cor = dados.cor();
        }
    }

    public void inativar(){
        this.ativo = false;
    }
}
