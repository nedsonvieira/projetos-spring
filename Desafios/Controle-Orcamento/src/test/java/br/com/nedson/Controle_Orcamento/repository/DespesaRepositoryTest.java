package br.com.nedson.Controle_Orcamento.repository;

import br.com.nedson.Controle_Orcamento.model.Categoria;
import br.com.nedson.Controle_Orcamento.model.Despesa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DespesaRepositoryTest {

    @Autowired
    private DespesaRepository despesaRepository;

    @BeforeEach
    void setUp() {
        despesaRepository.deleteAll();

        var despesa = new Despesa(null, "Aluguel", new BigDecimal("1200.00"), LocalDate.of(2024, 3, 10), Categoria.MORADIA);
        despesaRepository.save(despesa);
    }

    @Test
    void deveVerificarSeExisteDespesaDuplicadaNoMesmoMes() {
        var existe = despesaRepository.existsByDescricaoAndDataBetween(
                "Aluguel",
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 31));

        assertTrue(existe);
    }

    @Test
    void deveBuscarDespesasPorDescricaoIgnorandoCase() {
        var pageable = PageRequest.of(0, 10);

        var response = despesaRepository.findByDescricaoContainingIgnoreCase("aluguel", pageable);

        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    void deveBuscarDespesasPorAnoEMes() {
        var pageable = PageRequest.of(0, 10);

        var response = despesaRepository.findByAnoAndMes(2024, 3, pageable);

        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    void deveCalcularTotalDespesasPorMes() {
        despesaRepository.save(new Despesa(null, "Internet", new BigDecimal("150.00"), LocalDate.of(2024, 3, 5), Categoria.MORADIA));

        var total = despesaRepository.calcularTotalDespesasPorMes(2024, 3);

        assertThat(total).isPresent().contains(new BigDecimal("1350.00"));
    }

    @Test
    void deveCalcularTotalPorCategoria() {
        despesaRepository.save(new Despesa(null, "Internet", new BigDecimal("100.00"), LocalDate.of(2024, 3, 5), Categoria.MORADIA));
        despesaRepository.save(new Despesa(null, "Mercado", new BigDecimal("300.00"), LocalDate.of(2024, 3, 5), Categoria.ALIMENTACAO));

        var response = despesaRepository.calcularTotalPorCategoria(2024, 3);

        assertThat(response).hasSize(2);
    }
}