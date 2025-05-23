package br.com.nedson.Controle_Orcamento.validation.receita;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;

public interface ReceitaValidation {
    void validar(ReceitaCadastrarDTO dto);
    void validar(ReceitaAtualizarDTO dto);
}
