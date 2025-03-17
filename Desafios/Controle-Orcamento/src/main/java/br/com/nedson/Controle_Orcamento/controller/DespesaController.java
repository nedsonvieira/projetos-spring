package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.dto.despesa.DespesaDetalharDTO;
import br.com.nedson.Controle_Orcamento.service.DespesaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/despesas")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class DespesaController {

    private final DespesaService despesaService;

    @PostMapping
    public ResponseEntity<DespesaDetalharDTO> cadastrar(@RequestBody @Valid DespesaCadastrarDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<DespesaDetalharDTO>> listar(@RequestParam(required = false) String descricao, Pageable paginacao){
        if (descricao == null || descricao.isBlank()) {
            return ResponseEntity.ok(despesaService.listarByDescricao("", paginacao));
        }
        return ResponseEntity.ok(despesaService.listarByDescricao(descricao, paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDetalharDTO> listarById(@PathVariable Long id){
        return ResponseEntity.ok(despesaService.listarById(id));
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<Page<DespesaDetalharDTO>> listarReceitasByAnoEMes(
            @PathVariable int ano, @PathVariable int mes, Pageable pageable) {
        return ResponseEntity.ok(despesaService.listarByAnoAndMes(ano, mes, pageable));
    }

    @PutMapping
    public ResponseEntity<DespesaDetalharDTO> atualizar(@RequestBody @Valid DespesaAtualizarDTO dto) {
        return ResponseEntity.ok(despesaService.atualizar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        despesaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    //Facilitar a população do banco
    @PostMapping("/lista")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid List<DespesaCadastrarDTO> dtos){
        despesaService.cadastrarLista(dtos);
        return ResponseEntity.ok().build();
    }
}