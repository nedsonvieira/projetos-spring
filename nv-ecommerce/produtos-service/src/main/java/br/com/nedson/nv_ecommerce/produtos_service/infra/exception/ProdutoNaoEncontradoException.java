package br.com.nedson.nv_ecommerce.produtos_service.infra.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(String msg) {
        super(msg);
    }
}
