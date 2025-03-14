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
        var despesa = despesaService.cadastrar(dto);
        return ResponseEntity.ok(despesa);
    }

    @GetMapping
    public ResponseEntity<Page<DespesaDetalharDTO>> listar(Pageable paginacao){
        var despesas = despesaService.listarAll(paginacao);
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDetalharDTO> listarById(@PathVariable Long id){
        var despesa = despesaService.listarById(id);
        return ResponseEntity.ok(despesa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaDetalharDTO> atualizar(@RequestBody @Valid DespesaAtualizarDTO dto) {
        var despesa = despesaService.atualizar(dto);
        return ResponseEntity.ok(despesa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        despesaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lista")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid List<DespesaCadastrarDTO> dtos){
        despesaService.cadastrarLista(dtos);
        return ResponseEntity.ok().build();
    }
}
