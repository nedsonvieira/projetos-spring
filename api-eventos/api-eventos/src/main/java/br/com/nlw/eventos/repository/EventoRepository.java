package br.com.nlw.eventos.repository;

import br.com.nlw.eventos.model.Evento;
import org.springframework.data.repository.CrudRepository;

/*
Esta interface amplia um interface propria do JPA, que é parametrizada
com 2 tipos (a classe que vai ser armazenada e o tipo do atributo que
identifica unicamente esta classe)

A interface CrudRepository tem as funcionalidades básicas prontas de um CRUD

Possibilita criar métodos específicos para outras recuperações
*/

public interface EventoRepository extends CrudRepository<Evento, Long> {
    public Evento findByPrettyName(String prettyName);
}
