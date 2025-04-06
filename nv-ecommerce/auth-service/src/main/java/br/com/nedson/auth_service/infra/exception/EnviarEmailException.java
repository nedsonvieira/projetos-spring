package br.com.nedson.auth_service.infra.exception;

public class EnviarEmailException extends RuntimeException{
    public EnviarEmailException(String msg){
        super(msg);
    }
}