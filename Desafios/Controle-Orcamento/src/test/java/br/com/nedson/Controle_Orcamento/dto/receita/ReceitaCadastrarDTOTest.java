package br.com.nedson.Controle_Orcamento.dto.receita;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceitaCadastrarDTOTest {

    @Test
    public void testConverteDataValida() {
        var dataString = "15/02/2025";
        var dto = new ReceitaCadastrarDTO("Salário", new BigDecimal("1500.00"), dataString);

        var dataConvertida = dto.converteData();
        assertEquals(LocalDate.of(2025, 2, 15), dataConvertida);
    }
}