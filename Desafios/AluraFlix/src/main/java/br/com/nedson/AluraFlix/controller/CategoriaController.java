package br.com.nedson.AluraFlix.controller;

import br.com.nedson.AluraFlix.dto.categoria.DadosAtualizarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosBuscarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosCadastrarCategoria;
import br.com.nedson.AluraFlix.dto.categoria.DadosDetalharCategoria;
import br.com.nedson.AluraFlix.dto.categoria.validacoes.ValidadorCategorias;
import br.com.nedson.AluraFlix.model.Categoria;
import br.com.nedson.AluraFlix.repository.CategoriaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/categorias")
@SecurityRequirement(name = "bearer-key")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    private final List<ValidadorCategorias> validacoes;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid DadosCadastrarCategoria dados){
        validacoes.forEach(v -> v.validar(dados));
        var categoria = new Categoria(dados);
        categoriaRepository.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalharCategoria(categoria));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalharCategoria>> listar(@PageableDefault(size = 5) Pageable paginacao){
        var page = categoriaRepository.findAllByAtivoTrue(paginacao)
                .map(DadosDetalharCategoria::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalharCategoria> listarById(@PathVariable Long id){
        var categoria = categoriaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada!"));
        return ResponseEntity.ok(new DadosDetalharCategoria(categoria));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalharCategoria> atualizar(@RequestBody @Valid DadosAtualizarCategoria dados){
        var categoria = categoriaRepository.findByIdAndAtivoTrue(dados.id())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada!"));
        validacoes.forEach(v -> v.validar(dados));

        categoria.atualizar(dados);

        return ResponseEntity.ok(new DadosDetalharCategoria(categoria));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable Long id){
        var categoria = categoriaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada!"));
        categoria.inativar();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Categoria deletada com sucesso!");
    }
}
