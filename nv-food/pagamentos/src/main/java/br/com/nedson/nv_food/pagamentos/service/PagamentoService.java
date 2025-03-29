package br.com.nedson.nv_food.pagamentos.service;

import br.com.nedson.nv_food.pagamentos.dto.PagamentoDto;
import br.com.nedson.nv_food.pagamentos.http.PedidoClient;
import br.com.nedson.nv_food.pagamentos.model.Pagamento;
import br.com.nedson.nv_food.pagamentos.model.Status;
import br.com.nedson.nv_food.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    private final ModelMapper modelMapper;

    private final PedidoClient pedidoClient;

    public PagamentoDto criarPagamento(PagamentoDto dto) {
        var pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamentoRepository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public Page<PagamentoDto> obterTodos(Pageable paginacao) {
        return pagamentoRepository
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, PagamentoDto.class));
    }

    public PagamentoDto obterPorId(Long id) {
        var pagamento = pagamentoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto) {
        if (!pagamentoRepository.existsById(id)){
            throw new EntityNotFoundException();
        }

        var pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamento = pagamentoRepository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public void excluirPagamento(Long id) {
        pagamentoRepository.deleteById(id);
    }

    public void confirmarPagamento(Long id){
        var pagamento = pagamentoRepository.findById(id);

        if (pagamento.isEmpty()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        pagamentoRepository.save(pagamento.get());
        pedidoClient.atualizaPagamento(pagamento.get().getPedidoId());
    }

    public void alteraStatus(Long id) {
        var pagamento = pagamentoRepository.findById(id);

        if (pagamento.isEmpty()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        pagamentoRepository.save(pagamento.get());

    }
}
