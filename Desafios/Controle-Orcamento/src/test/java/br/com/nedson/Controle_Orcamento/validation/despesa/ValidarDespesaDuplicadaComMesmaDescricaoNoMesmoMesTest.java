package br.com.nedson.Controle_Orcamento.validation.despesa;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.DespesaDuplicadaException;
import br.com.nedson.Controle_Orcamento.model.Categoria;
import br.com.nedson.Controle_Orcamento.repository.DespesaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class ValidarDespesaDuplicadaComMesmaDescricaoNoMesmoMesTest {

    @Mock
    private DespesaRepository despesaRepository;

    @InjectMocks
    private ValidarDespesaDuplicadaComMesmaDescricaoNoMesmoMes validacao;

    private DespesaCadastrarDTO dtoCadastrar;

    private DespesaAtualizarDTO dtoAtualizar;

    @BeforeEach
    void setUp() {
        dtoCadastrar = new DespesaCadastrarDTO("Aluguel", new BigDecimal("1200.00"), "10/03/2024", Categoria.MORADIA);
        dtoAtualizar = new DespesaAtualizarDTO(1L, "Aluguel", new BigDecimal("1200.00"), "10/03/2024", Categoria.MORADIA);
    }

    @Test
    @DisplayName("Não deve lançar exceção ao cadastrar e não houver despesa duplicada")
    void validarCadastrarSemDespesaDuplicada() {
        when(despesaRepository.existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(false);

        assertDoesNotThrow(() -> validacao.validar(dtoCadastrar));

        verify(despesaRepository, times(1)).existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar e houver despesa duplicada no mesmo mês")
    void validarCadastrarComDespesaDuplicada() {
        when(despesaRepository.existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        assertThrows(DespesaDuplicadaException.class, () -> validacao.validar(dtoCadastrar));

        verify(despesaRepository, times(1)).existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Não deve lançar exceção ao atualizar e não houver despesa duplicada")
    void validarAtualizarSemDespesaDuplicada() {
        when(despesaRepository.existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(false);

        assertDoesNotThrow(() -> validacao.validar(dtoAtualizar));

        verify(despesaRepository, times(1)).existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar e houver despesa duplicada no mesmo mês")
    void validarAtualizarComDespesaDuplicada() {
        when(despesaRepository.existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        assertThrows(DespesaDuplicadaException.class, () -> validacao.validar(dtoAtualizar));

        verify(despesaRepository, times(1)).existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }
}