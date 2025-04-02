package br.com.nedson.api_med_voll.dto.medico;

import br.com.nedson.api_med_voll.model.Medico;

public record DadosBuscarMedico(Long id,
                                String nome,
                                String email,
                                String crm,
                                Especialidade especialidade) {

    public DadosBuscarMedico(Medico medico){
        this(medico.getId(),
                medico.getNome(),
                medico.getEmail(),
                medico.getCrm(),
                medico.getEspecialidade());
    }
}
