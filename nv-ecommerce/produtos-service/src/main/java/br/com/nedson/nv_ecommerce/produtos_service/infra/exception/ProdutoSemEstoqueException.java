package br.com.nedson.nv_ecommerce.produtos_service.infra.exception;

public class ProdutoSemEstoqueException extends RuntimeException {
    public ProdutoSemEstoqueException(String msg) {
        super(msg);
    }
}
