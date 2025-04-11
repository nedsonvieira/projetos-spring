package br.com.nedson.nv_ecommerce.produtos_service.infra.exception;

public class ParametroInvalidoException extends RuntimeException {
    public ParametroInvalidoException(String msg) {
        super(msg);
    }
}
