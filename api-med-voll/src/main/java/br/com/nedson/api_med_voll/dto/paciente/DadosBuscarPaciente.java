package br.com.nedson.api_med_voll.dto.paciente;

import br.com.nedson.api_med_voll.model.Paciente;

public record DadosBuscarPaciente(String nome,
                                  String email,
                                  String cpf) {

    public DadosBuscarPaciente(Paciente paciente) {
        this(paciente.getNome(),
                paciente.getEmail(),
                paciente.getCpf());
    }
}

