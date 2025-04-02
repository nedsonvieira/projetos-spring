package br.com.nedson.api_med_voll.repository;

import br.com.nedson.api_med_voll.dto.endereco.EnderecoDTO;
import br.com.nedson.api_med_voll.dto.medico.DadosCadastrarMedico;
import br.com.nedson.api_med_voll.dto.medico.Especialidade;
import br.com.nedson.api_med_voll.dto.paciente.DadosCadastrarPaciente;
import br.com.nedson.api_med_voll.model.Consulta;
import br.com.nedson.api_med_voll.model.Medico;
import br.com.nedson.api_med_voll.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository repositorio;

    @Autowired
    private TestEntityManager em;

    private LocalDateTime proximaSegundaAs10;
    private Medico medico;
    private Paciente paciente;

    @BeforeEach
    void setup(){
        //given ou arrange
        proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.TUESDAY))
                .atTime(10,0);

        medico = cadastrarMedico("Medico", "medico@med.voll", "999999", Especialidade.CARDIOLOGIA);
        paciente = cadastrarPaciente("Paciente", "paciente@med.voll", "999.999.999-99");
    }

    @Test
    @DisplayName("Deveria retornar null quando único médico cadastrado não está disponível na data")
    void escolherMedicoAleatorioLivreNaDataCenario1() {
        cadastrarConsulta(medico, paciente, proximaSegundaAs10);

        //when ou act
        var medicoLivre = repositorio.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        //then ou assert
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deveria devolver médico quando ele estiver disponível na data")
    void escolherMedicoAleatorioLivreNaDataCenario2() {

        var medicoLivre = repositorio.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        assertThat(medicoLivre).isEqualTo(medico);
    }

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
        var consulta = new Consulta(null, medico, paciente, data, null);
        em.persist(consulta);
    }

    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
        em.persist(medico);
        return medico;
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf) {
        var paciente = new Paciente(dadosPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }

    private DadosCadastrarMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastrarMedico(
                nome,
                email,
                "61999999999",
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private DadosCadastrarPaciente dadosPaciente(String nome, String email, String cpf) {
        return new DadosCadastrarPaciente(
                nome,
                email,
                "61999999999",
                cpf,
                dadosEndereco()
        );
    }

    private EnderecoDTO dadosEndereco() {
        return new EnderecoDTO(
                "rua xpto",
                "bairro",
                "00000-000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}