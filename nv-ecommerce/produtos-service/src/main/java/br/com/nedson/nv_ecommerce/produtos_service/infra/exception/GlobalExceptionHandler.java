package br.com.nedson.nv_ecommerce.produtos_service.infra.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(
            GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ProdutoSemEstoqueException.class)
    public ResponseEntity<Map<String, String>> handleProdutoSemEstoqueException(
            ProdutoSemEstoqueException ex) {
        log.warn("Produto sem estoque {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Produto sem estoque");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ProdutoJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleProdutoJaCadastradoException(
            ProdutoJaCadastradoException ex) {
        log.warn("Produto já cadastrado {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Produto já cadastrado");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleProdutoNaoEncontradoException(
            ProdutoNaoEncontradoException ex) {
        log.warn("Produto não encontrado {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Produto não encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(CategoriaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleCategoriaNaoEncontradaException(
            CategoriaNaoEncontradaException ex) {
        log.warn("Categoria não encontrada {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Categoria não encontrada");
        errors.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}
