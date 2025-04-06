package br.com.nedson.auth_service.infra.exception;

public class EmailCadastradoException extends RuntimeException {
    public EmailCadastradoException(String msg) {
        super(msg);
    }
}
