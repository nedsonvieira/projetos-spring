package br.com.nlw.eventos.controller;

import br.com.nlw.eventos.model.Evento;
import br.com.nlw.eventos.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    @RestController indica que a classe é um controlador REST, responsável
    por manipular requisições HTTP.
    Equivale a usar @Controller + @ResponseBody, ou seja, todos
    os métodos retornam automaticamente respostas JSON ou XML.

    É importante porque simplifica a criação de APIs RESTful,
    já que evita a necessidade de usar @ResponseBody em cada função.
*/
@RestController
public class EventoController {

    @Autowired
    private EventoService servico;

    @PostMapping("/eventos") // Mapeia o métod0 como uma URL para lidar com requisições HTTP POST (enviar dados).
    public Evento cadastrarEvento(@RequestBody Evento evento){
        /*
        Indica que o parâmetro da função será extraído do corpo da requisição (JSON) HTTP e
        convertido automaticamente em um objeto Java.
        Utiliza a biblioteca Jackson (ou outra configurada) para converter JSON em objetos Java.

        É importante, poís facilita o envio de dados complexos no corpo da requisição, como objetos JSON.
        */
        return servico.cadastrarEvento(evento);
    }

    @GetMapping("/eventos") // Mapeia o métod0 como uma URL para lidar com requisições HTTP GET.
    public List<Evento> getAllEventos(){
        return servico.getAllEventos();
    }

    /*    ResponseEntity é uma classe do Spring usada para representar toda a
    resposta HTTP, incluindo o código de status, cabeçalhos e corpo da resposta.
    Ela é usada para personalizar a resposta que a API retorna ao cliente.
     */
    @GetMapping("eventos/{prettyName}")
    public ResponseEntity<Evento> listarEventoByPrettyName(@PathVariable String prettyName){
        /*
        O @PathVariable permite capturar valores dinâmicos da URL e usá-los como parâmetros do métod0.
        Muito útil para buscar, atualizar ou deletar recursos específicos com base em seu identificador.
         */
        Evento evt = servico.listarByPrettyName(prettyName);
         if (evt != null){ // evento existe no banco de dados
            return ResponseEntity.ok().body(evt);
         }
         return ResponseEntity.notFound().build(); // retorna o codigo de erro 404 not found
    }

}
