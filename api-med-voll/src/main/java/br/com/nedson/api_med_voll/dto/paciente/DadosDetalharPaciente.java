package br.com.nedson.api_med_voll.dto.paciente;

import br.com.nedson.api_med_voll.model.Endereco;
import br.com.nedson.api_med_voll.model.Paciente;

public record DadosDetalharPaciente(Long id,
                                    String nome,
                                    String email,
                                    String cpf,
                                    String telefone,
                                    Endereco endereco) {

    public DadosDetalharPaciente(Paciente paciente){
        this(paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getCpf(),
                paciente.getTelefone(),
                paciente.getEndereco());
    }
}

