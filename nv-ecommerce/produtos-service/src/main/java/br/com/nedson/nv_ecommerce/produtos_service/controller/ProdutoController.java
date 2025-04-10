package br.com.nedson.nv_ecommerce.produtos_service.controller;

import br.com.nedson.nv_ecommerce.produtos_service.domain.Categoria;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoAtualizarRequestDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoCadastrarRequestDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoResponseDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nv-ecommerce/produtos")
@AllArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrar(@Valid @RequestBody ProdutoCadastrarRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoService.cadastrarProduto(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDto>> listarTodos(@RequestParam(required = false) String categoria, Pageable pageable) {
        if (categoria == null) {
            return ResponseEntity.ok(produtoService.listarTodosProdutos(pageable));
        }
        return ResponseEntity.ok(produtoService.listarPorCategoria(categoria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> listarPorId(@PathVariable String id) {
        return ResponseEntity.ok(produtoService.listarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable String id, @Valid @RequestBody ProdutoAtualizarRequestDto dto) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        return ResponseEntity.noContent().build();
    }
}
