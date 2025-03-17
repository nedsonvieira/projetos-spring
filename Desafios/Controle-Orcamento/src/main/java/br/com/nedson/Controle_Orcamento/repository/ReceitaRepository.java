package br.com.nedson.Controle_Orcamento.repository;

import br.com.nedson.Controle_Orcamento.model.Receita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Boolean existsByDescricaoAndDataBetween(String descricao, LocalDate inicioMes, LocalDate fimMes);

    @Query("""
            SELECT r FROM Receita r
             WHERE LOWER(r.descricao)
             LIKE LOWER(CONCAT('%', :descricao, '%'))
            """)
    Page<Receita> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    @Query("""
            SELECT r FROM Receita r
             WHERE YEAR(r.data) = :ano
             AND MONTH(r.data) = :mes
            """)
    Page<Receita> findByAnoAndMes(int ano, int mes, Pageable pageable);

    @Query("""
            SELECT SUM(r.valor) FROM Receita r
             WHERE YEAR(r.data) = :ano
             AND MONTH(r.data) = :mes
            """)
    Optional<BigDecimal> calcularTotalReceitasPorMes(int ano, int mes);
}
