package br.com.nedson.api_med_voll.repository;

import br.com.nedson.api_med_voll.model.Paciente;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Page<Paciente> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
            SELECT p.ativo FROM Paciente p
            WHERE p.id = :idPaciente
            """)
    Boolean findAtivoById(Long idPaciente);
}
