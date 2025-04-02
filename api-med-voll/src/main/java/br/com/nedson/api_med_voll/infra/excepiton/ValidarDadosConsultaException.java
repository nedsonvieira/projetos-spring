package br.com.nedson.api_med_voll.infra.excepiton;

public class ValidarDadosConsultaException extends RuntimeException {

    public ValidarDadosConsultaException(String msg) {
        super(msg);
    }
}
