package br.com.nlw.events.service;

import br.com.nlw.events.model.Evento;
import br.com.nlw.events.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 Tod0 Service em java precisa ter a anotação @Service para que o SpringBoot
 gerencie a criação desse service.
 */

@Service
public class EventoService {

    /*
    A anotação @Autowired permite a injeção automática de dependências pelo Spring.
    Evita a necessidade de criar instâncias manualmente com new.
    O Spring gerencia a criação e injeção do objeto automaticamente.

    Neste caso pelo fato do IEventRepo ser um repositório e extender o CrudRepo
    o springboot vai buscar a implementação do crudRepo com os parametros Event
    e Integer, e depois criar um objeto para ser utilizado.

    Resumindo: o spring busca uma implementação e cria uma instância dessa implementação
    e deixa disponível para ser usada.
    */
    @Autowired
    private EventoRepository repositorio;

    /*
    Passa um evento com alguns dados, salva no banco de dados e retorna o evento
    com os dados preenchidos e tudo que foi gravado no banco
    (adiciona o id e o pretty name a partir no titulo)
     */
    public Evento cadastrarEvento(Evento evento){
        // gerando o pretty name
        evento.setPrettyName(evento.getTitulo().toLowerCase().replaceAll(" ", "-"));
        return repositorio.save(evento);
    }

    //Retorna todos os eventos salvos
    public List<Evento> getAllEventos(){
        return (List<Evento>) repositorio.findAll();
    }

    //busca por prettyname
    public Evento listarByPrettyName(String prettyName){
        return repositorio.findByPrettyName(prettyName);
    }

    /*
    Obs: o Spring data (framework que usa o JPA) permite criar definições de
    e esses nomes de metódos são manipulados pelo JPA, e cria uma string SQL
    correspondente.
    Os metódos que começam com findBy, getBy, readBy + o nome do atributo permitem
    recuperar os dados como uma busca where pretty_name = ?
     */

}
