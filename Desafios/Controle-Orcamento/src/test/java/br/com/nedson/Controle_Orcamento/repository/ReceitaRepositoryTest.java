package br.com.nedson.Controle_Orcamento.repository;

import br.com.nedson.Controle_Orcamento.model.Receita;
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
class ReceitaRepositoryTest {

    @Autowired
    private ReceitaRepository receitaRepository;

    @BeforeEach
    void setUp() {
        receitaRepository.deleteAll();

        var receita = new Receita(null, "Salário", new BigDecimal("5000.00"), LocalDate.of(2024, 3, 10));
        receitaRepository.save(receita);
    }

    @Test
    void deveVerificarExistenciaDeReceitaDuplicadaNoMesmoMes() {
        var existe = receitaRepository.existsByDescricaoAndDataBetween(
                "Salário",
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 31));

        assertTrue(existe);
    }

    @Test
    void deveBuscarReceitasPorDescricaoIgnorandoCase() {
        var pageable = PageRequest.of(0, 10);

        var response = receitaRepository.findByDescricaoContainingIgnoreCase("salário", pageable);

        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    void deveBuscarReceitasPorAnoEMes() {
        var pageable = PageRequest.of(0, 10);

        var response = receitaRepository.findByAnoAndMes(2024, 3, pageable);

        assertThat(response.getContent()).hasSize(1);
    }

    @Test
    void deveCalcularTotalDeReceitasPorMes() {
        receitaRepository.save(new Receita(null, "Freelancer", new BigDecimal("2000.00"), LocalDate.of(2024, 3, 10)));

        var total = receitaRepository.calcularTotalReceitasPorMes(2024, 3);

        assertThat(total).isPresent().contains(new BigDecimal("7000.00"));
    }
}