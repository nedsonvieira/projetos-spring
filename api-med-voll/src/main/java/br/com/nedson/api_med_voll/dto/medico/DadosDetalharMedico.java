package br.com.nedson.api_med_voll.dto.medico;

import br.com.nedson.api_med_voll.model.Endereco;
import br.com.nedson.api_med_voll.model.Medico;

public record DadosDetalharMedico(Long id,
                                  String nome,
                                  String email,
                                  String crm,
                                  String telefone,
                                  Especialidade especialidade,
                                  Endereco endereco) {

    public DadosDetalharMedico(Medico medico) {
        this(medico.getId(),
                medico.getNome(),
                medico.getEmail(),
                medico.getCrm(),
                medico.getTelefone(),
                medico.getEspecialidade(),
                medico.getEndereco());
    }
}
