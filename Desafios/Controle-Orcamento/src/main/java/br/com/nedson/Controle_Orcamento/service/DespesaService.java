package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaDetalharDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.ReceitaDuplicadaException;
import br.com.nedson.Controle_Orcamento.model.Despesa;
import br.com.nedson.Controle_Orcamento.repository.DespesaRepository;
import br.com.nedson.Controle_Orcamento.validation.despesa.DespesaValidation;
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

    public Page<DespesaDetalharDTO> listarAll(Pageable paginacao) {
        return despesaRepository.findAll(paginacao).map(DespesaDetalharDTO::new);
    }

    public DespesaDetalharDTO listarById(Long id) {
        var despesa = despesaRepository.getReferenceById(id);
        return new DespesaDetalharDTO(despesa);
    }

    @Transactional
    public DespesaDetalharDTO atualizar(@Valid DespesaAtualizarDTO dto) {
        var despesa = despesaRepository.getReferenceById(dto.id());

        if (dto.descricao() != null && !dto.descricao().equals(despesa.getDescricao())) {
            verificarDespesaDuplicada(dto.descricao(), despesa.getData());
        }
        despesa.atualizar(dto);

        return new DespesaDetalharDTO(despesa);
    }

    @Transactional
    public void deletar(Long id) {
        despesaRepository.deleteById(id);
    }

    @Transactional
    public void cadastrarLista(List<DespesaCadastrarDTO> dtos){
        List<Despesa> despesas = dtos.stream()
                .map(Despesa::new).toList();
        despesaRepository.saveAllAndFlush(despesas);
    }

    private void verificarDespesaDuplicada(String descricao, LocalDate data) {
        LocalDate inicioMes = data.withDayOfMonth(1);
        LocalDate fimMes = data.withDayOfMonth(data.lengthOfMonth());

        if (despesaRepository.existsByDescricaoAndDataBetween(descricao, inicioMes, fimMes)) {
            throw new ReceitaDuplicadaException("Já existe uma despesa com essa descrição neste mês!");
        }
    }
}