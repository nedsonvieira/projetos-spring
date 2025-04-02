package br.com.nedson.api_med_voll.controller;

import br.com.nedson.api_med_voll.dto.consulta.DadosAgendarConsulta;
import br.com.nedson.api_med_voll.dto.consulta.DadosCancelarConsulta;
import br.com.nedson.api_med_voll.dto.consulta.DadosDetalharConsulta;
import br.com.nedson.api_med_voll.service.ConsultaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    @Autowired
    private ConsultaService servicoConsulta;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendarConsulta dados){
        var consulta = servicoConsulta.agendar(dados);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DadosCancelarConsulta dados){
        servicoConsulta.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}
