package br.com.nedson.api_med_voll.controller;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.dto.consulta.DadosCancelarConsulta;
import br.com.nedson.api_med_voll.dto.consulta.DadosDetalharConsulta;
import br.com.nedson.api_med_voll.dto.consulta.MotivoCancelamento;
import br.com.nedson.api_med_voll.dto.medico.Especialidade;
import br.com.nedson.api_med_voll.service.ConsultaService;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendarConsulta> jsonRequestAgendar;

    @Autowired
    private JacksonTester<DadosCancelarConsulta> jsonRequestCancelar;

    @Autowired
    private JacksonTester<DadosDetalharConsulta> jsonResponse;

    @MockitoBean
    private ConsultaService consultaService;

    @Test
    @DisplayName("Deveria devolver codigo http 403 quando usuário não tiver logado")
    void agendar_cenario1() throws Exception {
        var response = mvc.perform(post("/consultas"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informações estão inválidas")
    @WithMockUser
    void agendar_cenario2() throws Exception {
        var response = mvc.perform(post("/consultas"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informações estão válidas")
    @WithMockUser
    void agendar_cenario3() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;

        var dadosDetalharConsulta = new DadosDetalharConsulta(null, 2L, 5L, data);

        when(consultaService.agendar(any())).thenReturn(dadosDetalharConsulta);

        var response = mvc.
                perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAgendar.write(new DadosAgendarConsulta(2L, 5L, data, especialidade)).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = jsonResponse.write(dadosDetalharConsulta).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 403 quando usuário não tiver logado")
    void cancelar_cenario1() throws Exception {
        var response = mvc.perform(delete("/consultas"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 204 quando a consulta for cancelada")
    @WithMockUser
    void cancelar_cenario2() throws Exception {
        var dadosCancelar = new DadosCancelarConsulta(1L, MotivoCancelamento.PACIENTE_DESISTIU);

        doNothing().when(consultaService).cancelar(any());

        var response = mvc.perform(delete("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestCancelar.write(dadosCancelar).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    @Test
    @DisplayName("Deveria devolver código HTTP 400 quando o motivo do cancelamento não for informado")
    @WithMockUser
    void cancelar_cenario3() throws Exception {
        var dadosCancelar = new DadosCancelarConsulta(1L, null); // Motivo ausente

        var response = mvc.perform(delete("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestCancelar.write(dadosCancelar).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    @DisplayName("Deveria devolver código HTTP 404 quando a consulta não existir")
    @WithMockUser
    void cancelar_cenario4() throws Exception {
        var dadosCancelar = new DadosCancelarConsulta(999L, MotivoCancelamento.PACIENTE_DESISTIU);

        // Simula o serviço lançando uma exceção quando a consulta não existe
        doThrow(new EntityNotFoundException("Consulta não encontrada")).when(consultaService).cancelar(any());

        var response = mvc.perform(delete("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestCancelar.write(dadosCancelar).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver código HTTP 409 quando a consulta já tiver sido cancelada")
    @WithMockUser
    void cancelar_cenario5() throws Exception {
        var dadosCancelar = new DadosCancelarConsulta(1L, MotivoCancelamento.PACIENTE_DESISTIU);

        // Simula o serviço lançando exceção quando a consulta já está cancelada
        doThrow(new IllegalStateException("A consulta já foi cancelada anteriormente"))
                .when(consultaService).cancelar(any());

        var response = mvc.perform(delete("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestCancelar.write(dadosCancelar).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}