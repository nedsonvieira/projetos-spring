package br.com.nlw.eventos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inscricoes")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_inscricao")
    private Long numeroInscricao;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario inscrito;

    @ManyToOne
    @JoinColumn(name = "indicacao_usuario_id", nullable = true)
    private Usuario indicacao;

    public Long getNumeroInscricao() {
        return numeroInscricao;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getInscrito() {
        return inscrito;
    }

    public void setInscrito(Usuario inscrito) {
        this.inscrito = inscrito;
    }

    public void setIndicacao(Usuario indicacao) {
        this.indicacao = indicacao;
    }
}
