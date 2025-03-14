package br.com.nedson.Controle_Orcamento.repository;

import br.com.nedson.Controle_Orcamento.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    Boolean existsByDescricaoAndDataBetween(String descricao, LocalDate inicioMes, LocalDate fimMes);
}
