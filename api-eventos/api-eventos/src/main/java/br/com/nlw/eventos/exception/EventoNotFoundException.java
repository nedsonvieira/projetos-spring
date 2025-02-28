package br.com.nlw.eventos.exception;

public class EventoNotFoundException extends RuntimeException{

    public EventoNotFoundException(String msg) {
        super(msg);
    }
}
