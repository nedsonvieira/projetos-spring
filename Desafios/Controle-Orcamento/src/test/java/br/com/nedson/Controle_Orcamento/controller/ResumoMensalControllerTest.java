package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.ResumoMensalDTO;
import br.com.nedson.Controle_Orcamento.model.Categoria;
import br.com.nedson.Controle_Orcamento.service.ResumoMensalService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ResumoMensalControllerTest {

    @MockitoBean
    private ResumoMensalService resumoMensalService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    @DisplayName("Deveria retornar 200 - OK - ao obter resumo mensal com ano e mês válidos")
    void obterResumoMensalComAnoEMesValidos() throws Exception {
        var resumo = new ResumoMensalDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("200.00"),
                new BigDecimal("800.00"),
                Map.of(Categoria.ALIMENTACAO, new BigDecimal("200.00"))
        );

        when(resumoMensalService.gerarResumo(eq(2025), eq(1))).thenReturn(resumo);

        mvc.perform(get("/resumo/2025/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReceitas", is(resumo.totalReceitas().doubleValue())))
                .andExpect(jsonPath("$.totalDespesas", is(resumo.totalDespesas().doubleValue())))
                .andExpect(jsonPath("$.saldoFinal", is(resumo.saldoFinal().doubleValue())))
                .andExpect(jsonPath("$.totalGastoPorCategoria", hasEntry("ALIMENTACAO", resumo.totalGastoPorCategoria().get(Categoria.ALIMENTACAO).doubleValue())));
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria retornar 400 - Bad Request - ao obter resumo com ano ou mês inválidos")
    void obterResumoMensalComAnoOuMesInvalidos() throws Exception {
        when(resumoMensalService.gerarResumo(anyInt(), anyInt()))
                .thenThrow(MethodArgumentTypeMismatchException.class);

        mvc.perform(get("/resumo/asd/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("deve ser um número inteiro válido")));
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria retornar 404 - Not Found - ao obter resumo de um mês sem registros")
    void obterResumoMensalSemRegistros() throws Exception {
        when(resumoMensalService.gerarResumo(eq(2025), eq(2)))
                .thenThrow(new EntityNotFoundException("Nenhum resumo encontrado para este período."));

        mvc.perform(get("/resumo/2025/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Nenhum resumo encontrado para este período.")));
    }
}