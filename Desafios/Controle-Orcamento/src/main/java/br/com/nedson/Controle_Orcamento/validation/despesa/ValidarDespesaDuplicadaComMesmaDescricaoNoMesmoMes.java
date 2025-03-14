package br.com.nedson.Controle_Orcamento.validation.despesa;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.DespesaDuplicadaException;
import br.com.nedson.Controle_Orcamento.repository.DespesaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ValidarDespesaDuplicadaComMesmaDescricaoNoMesmoMes implements DespesaValidation{

    private final DespesaRepository despesaRepository;

    @Override
    public void validar(DespesaCadastrarDTO dto) {
        LocalDate inicioMes = dto.converteData().withDayOfMonth(1);
        LocalDate fimMes = dto.converteData().withDayOfMonth(dto.converteData().lengthOfMonth());

        if (despesaRepository.existsByDescricaoAndDataBetween(dto.descricao(), inicioMes, fimMes)) {
            throw new DespesaDuplicadaException("Já existe uma despesa com essa descrição neste mês!");
        }
    }
}
