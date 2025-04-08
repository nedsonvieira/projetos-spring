package br.com.nedson.auth_service.infra.exception;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        logger.warn("Erro 404 capturado: {}", ex.getMessage());
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EmailCadastradoException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(EmailCadastradoException ex) {
        logger.warn("Erro 409 capturado: {}", ex.getMessage());
        return createResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {
        List<DadosErroValidacao> erros = ex.getFieldErrors().stream()
                .map(DadosErroValidacao::new)
                .toList();
        logger.warn("Erro 400 capturado - Validação: {}", erros);
        return createResponse(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        logger.warn("Erro 400 capturado - Mensagem não legível: {}", ex.getMessage());
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String mensagem = String.format("O parâmetro '%s' deve ser um email válido!", ex.getName());
        logger.warn("Erro 400 capturado - Tipo de argumento: {}", mensagem);
        return createResponse(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.error("Erro 400 capturado - Violação de integridade: {}", ex.getMessage());
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidToken(TokenInvalidoException ex) {
        logger.warn("Erro 400 capturado - Token inválido: {}", ex.getMessage());
        return createResponse(HttpStatus.BAD_REQUEST, "Token inválido: " + ex.getMessage());
    }

    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<ApiErrorResponse> handleTokenExpired(TokenExpiradoException ex) {
        logger.warn("Erro 401 capturado - Token expirado: {}", ex.getMessage());
        return createResponse(HttpStatus.UNAUTHORIZED, "Token expirado: " + ex.getMessage());
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidToken(JWTVerificationException ex) {
        logger.warn("Erro 401 capturado - Erro na validação do token: {}", ex.getMessage());
        return createResponse(HttpStatus.UNAUTHORIZED, "Erro ao validar: " + ex.getMessage());
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<String> handleTokenExpiredOrInvalid(JWTCreationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials() {
        logger.warn("Erro 401 capturado - Credenciais inválidas.");
        return createResponse(HttpStatus.UNAUTHORIZED, "EMAIL ou SENHA inválidos.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationFailure() {
        logger.warn("Erro 401 capturado - Falha na autenticação.");
        return createResponse(HttpStatus.UNAUTHORIZED, "Falha na autenticação.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied() {
        logger.warn("Erro 403 capturado - Acesso negado.");
        return createResponse(HttpStatus.FORBIDDEN, "Acesso negado.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericError(Exception ex) {
        logger.error("Erro 500 capturado: {}", ex.getLocalizedMessage());
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado: " + ex.getLocalizedMessage());
    }

    @Getter
    public static class ApiErrorResponse {
        private final String mensagem;
        private final int status;
        private Object detalhes;

        public ApiErrorResponse(String message, int statusCode) {
            this.mensagem = message;
            this.status = statusCode;
        }

        public ApiErrorResponse(String message, int statusCode, Object details) {
            this.mensagem = message;
            this.status = statusCode;
            this.detalhes = details;
        }
    }

    private ResponseEntity<ApiErrorResponse> createResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(message, status.value()));
    }

    private ResponseEntity<ApiErrorResponse> createResponse(Object details) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Erro de validação.", HttpStatus.BAD_REQUEST.value(), details));
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}


