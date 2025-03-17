package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaDetalharDTO;
import br.com.nedson.Controle_Orcamento.model.Receita;
import br.com.nedson.Controle_Orcamento.repository.ReceitaRepository;
import br.com.nedson.Controle_Orcamento.validation.receita.ReceitaValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<ReceitaDetalharDTO> listarByDescricao(String descricao, Pageable paginacao) {
        return receitaRepository.findByDescricaoContainingIgnoreCase(descricao, paginacao)
                .map(ReceitaDetalharDTO::new);
    }

    public ReceitaDetalharDTO listarById(Long id) {
        var receita = receitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada!"));

        return new ReceitaDetalharDTO(receita);
    }

    public Page<ReceitaDetalharDTO> listarByAnoAndMes(int ano, int mes, Pageable pageable) {
        return receitaRepository.findByAnoAndMes(ano, mes, pageable)
                .map(ReceitaDetalharDTO::new);
    }

    @Transactional
    public ReceitaDetalharDTO atualizar(@Valid ReceitaAtualizarDTO dto) {
        var receita = receitaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada!"));

        validacoes.forEach(v -> v.validar(dto));

        receita.atualizar(dto);

        return new ReceitaDetalharDTO(receita);
    }

    @Transactional
    public void deletar(Long id) {
        receitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada!"));

        receitaRepository.deleteById(id);
    }

    @Transactional
    public void cadastrarLista(List<ReceitaCadastrarDTO> dtos){
        List<Receita> receitas = dtos.stream()
                .map(Receita::new).toList();
        receitaRepository.saveAllAndFlush(receitas);
    }
}