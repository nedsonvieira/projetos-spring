package br.com.nlw.events.exception;

public class EventoNotFoundException extends RuntimeException{

    public EventoNotFoundException(String msg) {
        super(msg);
    }
}
