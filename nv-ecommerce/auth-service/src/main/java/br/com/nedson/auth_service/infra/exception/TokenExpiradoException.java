package br.com.nedson.auth_service.infra.exception;

public class TokenExpiradoException extends RuntimeException {
    public TokenExpiradoException(String msg){
        super(msg);
    }
}
