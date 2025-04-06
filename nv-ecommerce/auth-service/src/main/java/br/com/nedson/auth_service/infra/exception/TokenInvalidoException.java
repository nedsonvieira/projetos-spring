package br.com.nedson.auth_service.infra.exception;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String msg){
        super(msg);
    }
}
