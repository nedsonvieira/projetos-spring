package br.com.nedson.Controle_Orcamento.service;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.ReceitaDuplicadaException;
import br.com.nedson.Controle_Orcamento.model.Receita;
import br.com.nedson.Controle_Orcamento.repository.ReceitaRepository;
import br.com.nedson.Controle_Orcamento.validation.receita.ReceitaValidation;
import br.com.nedson.Controle_Orcamento.validation.receita.ValidarReceitaDuplicadaComMesmaDescricaoNoMesmoMes;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
class ReceitaServiceTest {

    @InjectMocks
    private ReceitaService receitaService;

    @Mock
    private ReceitaRepository receitaRepository;

    @Spy
    private List<ReceitaValidation> validacoes = new ArrayList<>();

    @Mock
    private ValidarReceitaDuplicadaComMesmaDescricaoNoMesmoMes validacao;

    private ReceitaCadastrarDTO dtoCadastrar;

    @Test
    void deveriaCadastrarReceita(){
        dtoCadastrar = new ReceitaCadastrarDTO("Salário", new BigDecimal("4000.00"), "10/02/2024");

        var response = receitaService.cadastrar(dtoCadastrar);

        assertEquals(dtoCadastrar.descricao(), response.descricao());
        assertEquals(dtoCadastrar.valor(), response.valor());
        assertEquals(dtoCadastrar.data(), response.data());
    }

    @Test
    void deveriaChamarValidadoresDeReceita(){
        dtoCadastrar = new ReceitaCadastrarDTO("Salário", new BigDecimal("4000.00"), "10/02/2024");
        validacoes.add(validacao);

        receitaService.cadastrar(dtoCadastrar);

        then(validacao).should().validar(dtoCadastrar);
    }

    @Test
    void deveriaChamarValidadoresDeReceitaELancarExcecao(){
        dtoCadastrar = new ReceitaCadastrarDTO("Salário", new BigDecimal("4000.00"), "10/02/2024");
        validacoes.add(validacao);

        doThrow(new ReceitaDuplicadaException("Já existe uma receita com essa descrição neste mês!"))
                .when(validacao).validar(dtoCadastrar);

        assertThrows(ReceitaDuplicadaException.class, () -> receitaService.cadastrar(dtoCadastrar));

        verify(receitaRepository, never()).save(any(Receita.class));
    }

    @Test
    void deveriaListarDReceitasPorDescricao() {
        var receita = new Receita(1L, "Salário", new BigDecimal("4000.00"), LocalDate.of(2024, 2, 10));
        var page = new PageImpl<>(List.of(receita));
        var pageable = PageRequest.of(0, 10);

        when(receitaRepository.findByDescricaoContainingIgnoreCase("salário", pageable)).thenReturn(page);

        var response = receitaService.listarByDescricao("salário", pageable);

        assertFalse(response.isEmpty());
        assertEquals(1, response.getTotalElements());
        assertEquals(receita.getDescricao(), response.getContent().getFirst().descricao());
        assertEquals(receita.getValor(), response.getContent().getFirst().valor());
        assertEquals(receita.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), response.getContent().getFirst().data());
    }

    @Test
    void deveRetornarReceitaPorId() {
        var receita = new Receita(1L, "Salário", new BigDecimal("4000.00"), LocalDate.of(2024, 2, 10));

        when(receitaRepository.findById(1L)).thenReturn(Optional.of(receita));

        var response = receitaService.listarById(1L);

        assertNotNull(response);
        assertEquals(receita.getDescricao(), response.descricao());
        assertEquals(receita.getValor(), response.valor());
        assertEquals(receita.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), response.data());
    }

    @Test
    void deveLancarExcecaoQuandoReceitaNaoForEncontradaPorId() {
        var id = 99L;
        when(receitaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> receitaService.listarById(id));
    }

    @Test
    void deveListarReceitasPorAnoEMes() {
        var receita = new Receita(1L, "Salário", new BigDecimal("4000.00"), LocalDate.of(2024, 2, 10));
        var page = new PageImpl<>(List.of(receita));
        var pageable = PageRequest.of(0, 10);

        when(receitaRepository.findByAnoAndMes(2024, 2, pageable)).thenReturn(page);

        var response = receitaService.listarByAnoAndMes(2024, 2, pageable);

        assertFalse(response.isEmpty());
        assertEquals(1, response.getTotalElements());
        assertEquals(receita.getDescricao(), response.getContent().getFirst().descricao());
        assertEquals(receita.getValor(), response.getContent().getFirst().valor());
        assertEquals(receita.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), response.getContent().getFirst().data());
    }

    @Test
    void deveAtualizarReceitaComSucesso() {
        var id = 1L;
        var receita = new Receita(id, "Salário", new BigDecimal("4000.00"), LocalDate.of(2024, 2, 10));
        var dtoAtualizar = new ReceitaAtualizarDTO(id, "Novo Salário", new BigDecimal("5000.00"), "15/02/2024");

        when(receitaRepository.findById(id)).thenReturn(Optional.of(receita));

        var response = receitaService.atualizar(dtoAtualizar);

        assertNotNull(response);
        assertEquals(receita.getDescricao(), response.descricao());
        assertEquals(receita.getValor(), response.valor());
        assertEquals(receita.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), response.data());
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarReceitaNaoExistente() {
        var id = 99L;
        var dto = new ReceitaAtualizarDTO(id, "Nova Salário", new BigDecimal("5000.00"), "15/02/2024");

        when(receitaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> receitaService.atualizar(dto));
    }

    @Test
    void deveDeletarReceitaComSucesso() {
        var id = 1L;
        var receita = new Receita(id, "Salário", new BigDecimal("4000.00"), LocalDate.of(2024, 2, 10));

        when(receitaRepository.findById(id)).thenReturn(Optional.of(receita));
        doNothing().when(receitaRepository).deleteById(id);

        receitaService.deletar(id);

        verify(receitaRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoDeletarReceitaNaoExistente() {
        var id = 99L;
        when(receitaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> receitaService.deletar(id));
        verify(receitaRepository, never()).deleteById(id);
    }
}