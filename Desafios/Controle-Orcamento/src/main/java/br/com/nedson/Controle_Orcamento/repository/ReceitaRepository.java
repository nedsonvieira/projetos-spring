package br.com.nedson.Controle_Orcamento.repository;

import br.com.nedson.Controle_Orcamento.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Boolean existsByDescricaoAndDataBetween(String descricao, LocalDate inicioMes, LocalDate fimMes);
}
