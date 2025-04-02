package br.com.nedson.api_med_voll.model;

import br.com.nedson.api_med_voll.dto.paciente.DadosAtualizarPaciente;
import br.com.nedson.api_med_voll.dto.paciente.DadosCadastrarPaciente;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Paciente")
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private Boolean ativo;

    @Embedded
    private Endereco endereco;

    public Paciente(DadosCadastrarPaciente dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.endereco = new Endereco(dados.endereco());
        this.ativo = true;
    }

    public List<Paciente> converterLista(List<DadosCadastrarPaciente> dados) {
        return dados.stream()
                .map(p -> new Paciente(null, p.nome(),
                        p.email(), p.cpf(), p.telefone(), true, new Endereco(p.endereco())))
                .toList();
    }

    public void atualizarInfo(@Valid DadosAtualizarPaciente dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if (dados.endereco() != null) {
            endereco.atualizarInfo(dados.endereco());
        }
    }

    public void inativar() {
        this.ativo = false;
    }
}
