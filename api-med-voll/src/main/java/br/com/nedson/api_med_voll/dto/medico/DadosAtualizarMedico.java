package br.com.nedson.api_med_voll.dto.medico;

import br.com.nedson.api_med_voll.dto.endereco.EnderecoDTO;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizarMedico(
        @NotNull
        Long id,

        String nome,
        String telefone,
        EnderecoDTO endereco) {
}
