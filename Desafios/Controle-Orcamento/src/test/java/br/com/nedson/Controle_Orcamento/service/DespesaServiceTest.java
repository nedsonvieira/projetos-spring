package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.DespesaDuplicadaException;
import br.com.nedson.Controle_Orcamento.model.Categoria;
import br.com.nedson.Controle_Orcamento.model.Despesa;
import br.com.nedson.Controle_Orcamento.repository.DespesaRepository;
import br.com.nedson.Controle_Orcamento.validation.despesa.DespesaValidation;
import br.com.nedson.Controle_Orcamento.validation.despesa.ValidarDespesaDuplicadaComMesmaDescricaoNoMesmoMes;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespesaServiceTest {

    @InjectMocks
    private DespesaService despesaService;

    @Mock
    private DespesaRepository despesaRepository;

    @Spy
    private List<DespesaValidation> validacoes = new ArrayList<>();

    @Mock
    private ValidarDespesaDuplicadaComMesmaDescricaoNoMesmoMes validacao;

    private DespesaCadastrarDTO dtoCadastrar;

    @Test
    void deveriaCadastrarDespesa(){
        dtoCadastrar = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/02/2024", Categoria.MORADIA);

        var response = despesaService.cadastrar(dtoCadastrar);

        assertEquals(dtoCadastrar.descricao(), response.descricao());
        assertEquals(dtoCadastrar.valor(), response.valor());
        assertEquals(dtoCadastrar.data(), response.data());
        assertEquals(dtoCadastrar.categoria(), response.categoria());
    }

    @Test
    void deveriaChamarValidadoresDeDespesa(){
        dtoCadastrar = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/02/2024", Categoria.MORADIA);
        validacoes.add(validacao);

        despesaService.cadastrar(dtoCadastrar);

        then(validacao).should().validar(dtoCadastrar);
    }

    @Test
    void deveriaChamarValidadoresDeDespesaELancarExcecao(){
        dtoCadastrar = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/02/2024", Categoria.MORADIA);
        validacoes.add(validacao);

        doThrow(new DespesaDuplicadaException("Já existe uma despesa com essa descrição neste mês!"))
                .when(validacao).validar(dtoCadastrar);

        assertThrows(DespesaDuplicadaException.class, () -> despesaService.cadastrar(dtoCadastrar));

        verify(despesaRepository, never()).save(any(Despesa.class));
    }

    @Test
    void deveriaListarDespesasPorDescricao() {
        var despesa = new Despesa(1L, "Aluguel", new BigDecimal("1200.00"), LocalDate.of(2024, 2, 10), Categoria.MORADIA);
        var page = new PageImpl<>(List.of(despesa));
        var pageable = PageRequest.of(0, 10);

        when(despesaRepository.findByDescricaoContainingIgnoreCase("aluguel", pageable)).thenReturn(page);

        var response = despesaService.listarByDescricao("aluguel", pageable);

        assertFalse(response.isEmpty());
        assertEquals(1, response.getTotalElements());
        assertEquals(despesa.getDescricao(), response.getContent().getFirst().descricao());
        assertEquals(despesa.getValor(), response.getContent().getFirst().valor());
        assertEquals(despesa.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), response.getContent().getFirst().data());
        assertEquals(despesa.getCategoria(), response.getContent().getFirst().categoria());
    }

    @Test
    void deveRetornarDespesaPorId() {
        var despesa = new Despesa(1L, "Aluguel", new BigDecimal("1200.00"), LocalDate.of(2024, 2, 10), Categoria.MORADIA);

        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));

        var response = despesaService.listarById(1L);

        assertNotNull(response);
        assertEquals(despesa.getDescricao(), response.descricao());
        assertEquals(despesa.getValor(), response.valor());
        assertEquals(despesa.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), response.data());
        assertEquals(despesa.getCategoria(), response.categoria());
    }

    @Test
    void deveLancarExcecaoQuandoDespesaNaoForEncontradaPorId() {
        var id = 99L;
        when(despesaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> despesaService.listarById(id));
    }

    @Test
    void deveListarDespesasPorAnoEMes() {
        var despesa = new Despesa(1L, "Aluguel", new BigDecimal("1200.00"), LocalDate.of(2024, 2, 10), Categoria.MORADIA);
        var page = new PageImpl<>(List.of(despesa));
        var pageable = PageRequest.of(0, 10);

        when(despesaRepository.findByAnoAndMes(2024, 2, pageable)).thenReturn(page);

        var response = despesaService.listarByAnoAndMes(2024, 2, pageable);

        assertFalse(response.isEmpty());
        assertEquals(1, response.getTotalElements());
        assertEquals(despesa.getDescricao(), response.getContent().getFirst().descricao());
        assertEquals(despesa.getValor(), response.getContent().getFirst().valor());
        assertEquals(despesa.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), response.getContent().getFirst().data());
        assertEquals(despesa.getCategoria(), response.getContent().getFirst().categoria());
    }

    @Test
    void deveAtualizarDespesaComSucesso() {
        var id = 1L;
        var despesa = new Despesa(id, "Aluguel", new BigDecimal("1200.00"), LocalDate.of(2024, 2, 10), Categoria.MORADIA);
        var dtoAtualizar = new DespesaAtualizarDTO(id, "Novo Aluguel", new BigDecimal("1400.00"), "15/02/2024", Categoria.MORADIA);

        when(despesaRepository.findById(id)).thenReturn(Optional.of(despesa));

        var response = despesaService.atualizar(dtoAtualizar);

        assertNotNull(response);
        assertEquals(despesa.getDescricao(), response.descricao());
        assertEquals(despesa.getValor(), response.valor());
        assertEquals(despesa.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), response.data());
        assertEquals(despesa.getCategoria(), response.categoria());
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarDespesaNaoExistente() {
        var id = 99L;
        var dto = new DespesaAtualizarDTO(id, "Nova Aluguel", new BigDecimal("1400.00"), "15/02/2024", Categoria.MORADIA);

        when(despesaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> despesaService.atualizar(dto));
    }

    @Test
    void deveDeletarDespesaComSucesso() {
        var id = 1L;
        var despesa = new Despesa(id, "Aluguel", new BigDecimal("1200.00"), LocalDate.of(2024, 2, 10), Categoria.MORADIA);

        when(despesaRepository.findById(id)).thenReturn(Optional.of(despesa));
        doNothing().when(despesaRepository).deleteById(id);

        despesaService.deletar(id);

        verify(despesaRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoDeletarDespesaNaoExistente() {
        var id = 99L;
        when(despesaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> despesaService.deletar(id));
        verify(despesaRepository, never()).deleteById(id);
    }
}