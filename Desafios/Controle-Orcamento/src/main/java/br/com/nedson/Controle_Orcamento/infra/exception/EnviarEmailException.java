package br.com.nedson.Controle_Orcamento.infra.exception;

public class EnviarEmailException extends RuntimeException{
    public EnviarEmailException(String msg){
        super(msg);
    }
}
