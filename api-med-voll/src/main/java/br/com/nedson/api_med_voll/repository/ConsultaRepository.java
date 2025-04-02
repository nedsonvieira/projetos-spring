package br.com.nedson.api_med_voll.repository;

import br.com.nedson.api_med_voll.model.Consulta;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Boolean existsByPacienteIdAndDataBetween(Long idPaciente, LocalDateTime horarioPrimeiraConsulta, LocalDateTime horarioUltimaConsulta);

    Boolean existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(Long idMedico, LocalDateTime data);

    Boolean existsByIdAndMotivoCancelamentoIsNotNull(Long id);
}
