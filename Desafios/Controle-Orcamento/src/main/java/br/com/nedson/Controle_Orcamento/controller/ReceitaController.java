package br.com.nedson.Controle_Orcamento.controller;

import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaAtualizarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaCadastrarDTO;
import br.com.nedson.Controle_Orcamento.dto.receita.ReceitaDetalharDTO;
import br.com.nedson.Controle_Orcamento.service.ReceitaService;
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
@RequestMapping("/receitas")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class ReceitaController {

    private final ReceitaService receitaService;

    @PostMapping
    public ResponseEntity<ReceitaDetalharDTO> cadastrar(@RequestBody @Valid ReceitaCadastrarDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ReceitaDetalharDTO>> listar(@RequestParam(required = false) String descricao, Pageable paginacao){
        if (descricao == null || descricao.isBlank()) {
            return ResponseEntity.ok(receitaService.listarByDescricao("", paginacao));
        }
        return ResponseEntity.ok(receitaService.listarByDescricao(descricao, paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDetalharDTO> listarById(@PathVariable Long id){
        return ResponseEntity.ok(receitaService.listarById(id));
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<Page<ReceitaDetalharDTO>> listarReceitasPorAnoEMes(
            @PathVariable int ano, @PathVariable int mes, Pageable pageable) {
        return ResponseEntity.ok(receitaService.listarByAnoAndMes(ano, mes, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDetalharDTO> atualizar(@RequestBody @Valid ReceitaAtualizarDTO dto){
        return ResponseEntity.ok(receitaService.atualizar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        receitaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    //Facilitar a população do banco
    @PostMapping("/lista")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid List<ReceitaCadastrarDTO> dtos){
        receitaService.cadastrarLista(dtos);
        return ResponseEntity.ok().build();
    }
}