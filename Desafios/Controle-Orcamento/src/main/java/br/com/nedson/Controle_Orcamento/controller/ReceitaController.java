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
        var receita = receitaService.cadastrar(dto);
        return ResponseEntity.ok(receita);
    }

    @GetMapping
    public ResponseEntity<Page<ReceitaDetalharDTO>> listar(Pageable paginacao){
        var receitas = receitaService.listarAll(paginacao);
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDetalharDTO> listarById(@PathVariable Long id){
        var receita = receitaService.listarById(id);
        return ResponseEntity.ok(receita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDetalharDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ReceitaAtualizarDTO dto){
        var receita = receitaService.atualizar(id, dto);
        return ResponseEntity.ok(receita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        receitaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lista")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid List<ReceitaCadastrarDTO> dtos){
        receitaService.cadastrarLista(dtos);
        return ResponseEntity.ok().build();
    }
}
