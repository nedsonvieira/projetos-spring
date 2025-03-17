package br.com.nedson.Controle_Orcamento.infra.exception;

public class DespesaDuplicadaException extends RuntimeException {
    public DespesaDuplicadaException(String message) {
        super(message);
    }
}
