package br.com.nedson.Controle_Orcamento.validation.receita;


import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.infra.exception.ReceitaDuplicadaException;
import br.com.nedson.Controle_Orcamento.repository.ReceitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarReceitaDuplicadaComMesmaDescricaoNoMesmoMesTest {

    @Mock
    private ReceitaRepository receitaRepository;

    @InjectMocks
    private ValidarReceitaDuplicadaComMesmaDescricaoNoMesmoMes validacao;

    private ReceitaCadastrarDTO dtoCadastrar;

    private ReceitaAtualizarDTO dtoAtualizar;

    @BeforeEach
    void setUp() {
        dtoCadastrar = new ReceitaCadastrarDTO("Salário", new BigDecimal("1200.00"), "10/03/2024");
        dtoAtualizar = new ReceitaAtualizarDTO(1L, "Salário", new BigDecimal("1200.00"), "10/03/2024");
    }

    @Test
    @DisplayName("Não deve lançar exceção ao cadastrar e não houver receita duplicada")
    void validarCadastrarSemReceitaDuplicada() {
        when(receitaRepository.existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(false);

        assertDoesNotThrow(() -> validacao.validar(dtoCadastrar));

        verify(receitaRepository, times(1)).existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar e houver receita duplicada no mesmo mês")
    void validarCadastrarComReceitaDuplicada() {
        when(receitaRepository.existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        assertThrows(ReceitaDuplicadaException.class, () -> validacao.validar(dtoCadastrar));

        verify(receitaRepository, times(1)).existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Não deve lançar exceção ao atualizar e não houver receita duplicada")
    void validarAtualizarSemReceitaDuplicada() {
        when(receitaRepository.existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(false);

        assertDoesNotThrow(() -> validacao.validar(dtoAtualizar));

        verify(receitaRepository, times(1)).existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar e houver receita duplicada no mesmo mês")
    void validarAtualizarComReceitaDuplicada() {
        when(receitaRepository.existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        assertThrows(ReceitaDuplicadaException.class, () -> validacao.validar(dtoAtualizar));

        verify(receitaRepository, times(1)).existsByDescricaoAndDataBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }
}