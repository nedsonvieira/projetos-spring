package br.com.nedson.Controle_Orcamento.dto.despesa;

import br.com.nedson.Controle_Orcamento.model.Categoria;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DespesaCadastrarDTOTest {

    @Test
    public void testConverteDataValida() {
        var dataString = "15/02/2025";
        var dto = new DespesaCadastrarDTO("Compra", new BigDecimal("100.00"), dataString, Categoria.ALIMENTACAO);

        var dataConvertida = dto.converteData();
        assertEquals(LocalDate.of(2025, 2, 15), dataConvertida);
    }
}