package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaDetalharDTO;
import br.com.nedson.Controle_Orcamento.model.Despesa;
import br.com.nedson.Controle_Orcamento.repository.DespesaRepository;
import br.com.nedson.Controle_Orcamento.validation.despesa.DespesaValidation;
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
public class DespesaService {

    private final DespesaRepository despesaRepository;

    private final List<DespesaValidation> validacoes;

    @Transactional
    public DespesaDetalharDTO cadastrar(@Valid DespesaCadastrarDTO dto) {
        validacoes.forEach(v -> v.validar(dto));

        var despesa = new Despesa(dto);
        despesaRepository.save(despesa);

        return new DespesaDetalharDTO(despesa);
    }

    public Page<DespesaDetalharDTO> listarByDescricao(String descricao, Pageable paginacao) {
        return despesaRepository.findByDescricaoContainingIgnoreCase(descricao, paginacao)
                .map(DespesaDetalharDTO::new);
    }

    public DespesaDetalharDTO listarById(Long id) {
        var despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

        return new DespesaDetalharDTO(despesa);
    }

    public Page<DespesaDetalharDTO> listarByAnoAndMes(int ano, int mes, Pageable pageable) {
        return despesaRepository.findByAnoAndMes(ano, mes, pageable)
                .map(DespesaDetalharDTO::new);
    }

    @Transactional
    public DespesaDetalharDTO atualizar(@Valid DespesaAtualizarDTO dto) {
        var despesa = despesaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

        validacoes.forEach(v -> v.validar(dto));

        despesa.atualizar(dto);

        return new DespesaDetalharDTO(despesa);
    }

    @Transactional
    public void deletar(Long id) {
        despesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

        despesaRepository.deleteById(id);
    }

    @Transactional
    public void cadastrarLista(List<DespesaCadastrarDTO> dtos){
        List<Despesa> despesas = dtos.stream()
                .map(Despesa::new).toList();
        despesaRepository.saveAllAndFlush(despesas);
    }
}