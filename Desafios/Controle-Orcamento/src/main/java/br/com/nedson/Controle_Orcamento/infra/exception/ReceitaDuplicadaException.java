package br.com.nedson.Controle_Orcamento.infra.exception;

public class ReceitaDuplicadaException extends RuntimeException {
    public ReceitaDuplicadaException(String message) {
        super(message);
    }
}
