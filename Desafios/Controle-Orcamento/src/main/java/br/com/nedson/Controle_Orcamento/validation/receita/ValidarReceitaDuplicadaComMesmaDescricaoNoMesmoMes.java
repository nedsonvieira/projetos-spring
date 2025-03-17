package br.com.nedson.Controle_Orcamento.validation.receita;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.ReceitaDuplicadaException;
import br.com.nedson.Controle_Orcamento.repository.ReceitaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ValidarReceitaDuplicadaComMesmaDescricaoNoMesmoMes implements ReceitaValidation {

    private final ReceitaRepository receitaRepository;

    @Override
    public void validar(ReceitaCadastrarDTO dto) {
        LocalDate inicioMes = dto.converteData().withDayOfMonth(1);
        LocalDate fimMes = dto.converteData().withDayOfMonth(dto.converteData().lengthOfMonth());

        if (receitaRepository.existsByDescricaoAndDataBetween(dto.descricao(), inicioMes, fimMes)) {
            throw new ReceitaDuplicadaException("Já existe uma receita com essa descrição neste mês!");
        }
    }

    @Override
    public void validar(ReceitaAtualizarDTO dto) {
        LocalDate inicioMes = dto.converteData().withDayOfMonth(1);
        LocalDate fimMes = dto.converteData().withDayOfMonth(dto.converteData().lengthOfMonth());

        if (receitaRepository.existsByDescricaoAndDataBetween(dto.descricao(), inicioMes, fimMes)) {
            throw new ReceitaDuplicadaException("Já existe uma receita com essa descrição neste mês!");
        }
    }
}
