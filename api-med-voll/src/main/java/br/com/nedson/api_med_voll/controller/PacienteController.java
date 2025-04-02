package br.com.nedson.api_med_voll.controller;

import br.com.nedson.api_med_voll.repository.PacienteRepository;
import br.com.nedson.api_med_voll.dto.paciente.DadosAtualizarPaciente;
import br.com.nedson.api_med_voll.dto.paciente.DadosBuscarPaciente;
import br.com.nedson.api_med_voll.dto.paciente.DadosCadastrarPaciente;
import br.com.nedson.api_med_voll.dto.paciente.DadosDetalharPaciente;
import br.com.nedson.api_med_voll.model.Paciente;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    @Autowired
    private PacienteRepository repositorio;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastrarPaciente dados,
                          UriComponentsBuilder uriBuilder) {
        var paciente = new Paciente(dados);
        repositorio.save(paciente);

        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalharPaciente(paciente));
    }

//    @PostMapping
//    @Transactional
//    public void cadastrarAll(@RequestBody @Valid List<DadosCadastrarPaciente> dados) {
//        repositorio.saveAll(new Paciente().converterLista(dados));
//    }

    @GetMapping
    public ResponseEntity<Page<DadosBuscarPaciente>> listar(@PageableDefault( size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repositorio.findAllByAtivoTrue(paginacao).map(DadosBuscarPaciente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizarPaciente dados) {
        var paciente = repositorio.getReferenceById(dados.id());
        paciente.atualizarInfo(dados);

        return ResponseEntity.ok(new DadosDetalharPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id) {
        var paciente = repositorio.getReferenceById(id);
        paciente.inativar();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var paciente = repositorio.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalharPaciente(paciente));
    }
}