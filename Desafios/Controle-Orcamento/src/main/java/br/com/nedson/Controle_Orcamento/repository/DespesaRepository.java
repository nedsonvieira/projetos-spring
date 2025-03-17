package br.com.nedson.Controle_Orcamento.repository;

import br.com.nedson.Controle_Orcamento.model.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    Boolean existsByDescricaoAndDataBetween(String descricao, LocalDate inicioMes, LocalDate fimMes);

    @Query("""
            SELECT d FROM Despesa d
            WHERE LOWER(d.descricao)
            LIKE LOWER(CONCAT('%', :descricao, '%'))
            """)
    Page<Despesa> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    @Query("""
            SELECT d FROM Despesa d
             WHERE YEAR(d.data) = :ano
             AND MONTH(d.data) = :mes
            """)
    Page<Despesa> findByAnoAndMes(int ano, int mes, Pageable pageable);

    @Query("""
            SELECT SUM(d.valor) FROM Despesa d
             WHERE YEAR(d.data) = :ano
             AND MONTH(d.data) = :mes
            """)
    Optional<BigDecimal> calcularTotalDespesasPorMes(int ano, int mes);

    @Query("""
            SELECT d.categoria, SUM(d.valor) FROM Despesa d
             WHERE YEAR(d.data) = :ano
             AND MONTH(d.data) = :mes
             GROUP BY d.categoria
            """)
    List<Object[]> calcularTotalPorCategoria(int ano, int mes);
}
