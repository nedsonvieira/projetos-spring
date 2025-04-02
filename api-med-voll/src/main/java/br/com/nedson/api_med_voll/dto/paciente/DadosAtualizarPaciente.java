package br.com.nedson.api_med_voll.dto.paciente;

import br.com.nedson.api_med_voll.dto.endereco.EnderecoDTO;
import jakarta.validation.Valid;

public record DadosAtualizarPaciente(
        Long id,
        String nome,
        String telefone,
        @Valid EnderecoDTO endereco) {
}
