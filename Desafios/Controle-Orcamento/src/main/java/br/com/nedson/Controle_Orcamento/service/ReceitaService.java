package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaDetalharDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.ReceitaDuplicadaException;
import br.com.nedson.Controle_Orcamento.model.Receita;
import br.com.nedson.Controle_Orcamento.repository.ReceitaRepository;
import br.com.nedson.Controle_Orcamento.validation.receita.ReceitaValidation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReceitaService {

    private final ReceitaRepository receitaRepository;

    private final List<ReceitaValidation> validacoes;

    @Transactional
    public ReceitaDetalharDTO cadastrar(@Valid ReceitaCadastrarDTO dto) {
        validacoes.forEach(v -> v.validar(dto));

        var receita = new Receita(dto);
        receitaRepository.save(receita);

        return new ReceitaDetalharDTO(receita);
    }

    public Page<ReceitaDetalharDTO> listarAll(Pageable paginacao) {
        return receitaRepository.findAll(paginacao).map(ReceitaDetalharDTO::new);
    }

    public ReceitaDetalharDTO listarById(Long id) {
        var receita = receitaRepository.getReferenceById(id);
        return new ReceitaDetalharDTO(receita);
    }

    @Transactional
    public ReceitaDetalharDTO atualizar(Long id, @Valid ReceitaAtualizarDTO dto) {

        var receita = receitaRepository.getReferenceById(id);

        if (dto.descricao() != null && !dto.descricao().equals(receita.getDescricao())) {
            verificarReceitaDuplicada(dto.descricao(), receita.getData());
        }
        receita.atualizar(dto);

        return new ReceitaDetalharDTO(receita);
    }

    @Transactional
    public void deletar(Long id) {
        receitaRepository.deleteById(id);
    }

    @Transactional
    public void cadastrarLista(List<ReceitaCadastrarDTO> dtos){
        List<Receita> receitas = dtos.stream()
                .map(Receita::new).toList();
        receitaRepository.saveAllAndFlush(receitas);
    }

    private void verificarReceitaDuplicada(String descricao, LocalDate data) {
        LocalDate inicioMes = data.withDayOfMonth(1);
        LocalDate fimMes = data.withDayOfMonth(data.lengthOfMonth());

        if (receitaRepository.existsByDescricaoAndDataBetween(descricao, inicioMes, fimMes)) {
            throw new ReceitaDuplicadaException("Já existe uma receita com essa descrição neste mês!");
        }
    }
}