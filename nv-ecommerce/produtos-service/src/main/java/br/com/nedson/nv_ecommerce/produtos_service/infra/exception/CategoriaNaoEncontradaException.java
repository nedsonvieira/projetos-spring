package br.com.nedson.nv_ecommerce.produtos_service.infra.exception;

public class CategoriaNaoEncontradaException extends RuntimeException {
    public CategoriaNaoEncontradaException(String msg) {
        super(msg);
    }
}
