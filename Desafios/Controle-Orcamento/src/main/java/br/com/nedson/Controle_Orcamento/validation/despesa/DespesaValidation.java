package br.com.nedson.Controle_Orcamento.validation.despesa;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;

public interface DespesaValidation {
    void validar(DespesaCadastrarDTO dto);
}
