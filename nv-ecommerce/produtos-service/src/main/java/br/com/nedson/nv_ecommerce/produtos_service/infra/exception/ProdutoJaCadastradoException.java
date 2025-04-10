package br.com.nedson.nv_ecommerce.produtos_service.infra.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProdutoJaCadastradoException extends RuntimeException {
    public ProdutoJaCadastradoException(String msg) {
        super(msg);
    }
}
