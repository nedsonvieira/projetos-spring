package br.com.nedson.nv_ecommerce.produtos_service.controller;

import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoAtualizarRequestDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoCadastrarRequestDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.dto.ProdutoResponseDto;
import br.com.nedson.nv_ecommerce.produtos_service.domain.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/nv-ecommerce/produtos")
@AllArgsConstructor
@Tag(name = "Produto Controller", description = "Microsserviço para gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;


    @Operation(
            summary = "Cadastrar novo produto",
            description = "Endpoint para cadastro de novos produtos no sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro: Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Erro: Produto já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrar(@Valid @RequestBody ProdutoCadastrarRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrarProduto(dto));
    }

    @Operation(
            summary = "Listar todos os produtos",
            description = "Recupera uma lista paginada de todos os produtos ou filtra por categoria, se fornecida."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de produtos recuperada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro: Parâmetro informado inválido"),
            @ApiResponse(responseCode = "404", description = "Erro: Categoria não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDto>> listarTodos(@RequestParam(required = false) Map<String, String> categoria, Pageable pageable) {
        if (categoria.isEmpty()) {
            return ResponseEntity.ok(produtoService.listarTodosProdutos(pageable));
        }
        return ResponseEntity.ok(produtoService.listarPorCategoria(categoria, pageable));
    }

    @Operation(
            summary = "Recuperar produto por ID",
            description = "Recupera as informações de um produto específico pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Erro: Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> listarPorId(@PathVariable String id) {
        return ResponseEntity.ok(produtoService.listarPorId(id));
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza as informações de um produto específico pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro: Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Erro: Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable String id, @Valid @RequestBody ProdutoAtualizarRequestDto dto) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, dto));
    }

    @Operation(
            summary = "Excluir produto",
            description = "Exclui um produto específico pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Erro: Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}