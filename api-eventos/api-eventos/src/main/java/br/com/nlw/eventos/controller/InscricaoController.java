package br.com.nlw.events.controller;

import br.com.nlw.events.dto.MensagemErro;
import br.com.nlw.events.dto.InscricaoResponse;
import br.com.nlw.events.exception.EventoNotFoundException;
import br.com.nlw.events.exception.InscricaoConflictException;
import br.com.nlw.events.exception.UsuarioIndicadorNotFoundException;
import br.com.nlw.events.model.Usuario;
import br.com.nlw.events.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class InscricaoController {

    @Autowired
    private InscricaoService servico;

    /*
    Possui 2 tipos de padrões url, o primeiro faz uma inscrição em um evento pelo prettyname se o userid for null,
    o segundo verifica se já existe uma inscrição no evento verificando o prettyname e userid
    * */
    @PostMapping({"/inscricao/{prettyName}", "/inscricao/{prettyName}/{usuarioId}"})
    public ResponseEntity<?> cadastrarInscricao(@PathVariable String prettyName,
                                                @RequestBody Usuario inscrito,
                                                @PathVariable(required = false) Long usuarioId){

        try{
            InscricaoResponse resposta = servico.cadastrarInscricao(prettyName, inscrito, usuarioId);
            if (resposta != null){
                return ResponseEntity.ok(resposta);
            }

        } catch (EventoNotFoundException | UsuarioIndicadorNotFoundException ex){
            return ResponseEntity.status(404).body(new MensagemErro(ex.getMessage()));
        } catch (InscricaoConflictException ex){
            return  ResponseEntity.status(409).body(new MensagemErro(ex.getMessage()));
        }
        return  ResponseEntity.badRequest().build();
    }

    @GetMapping("/inscricao/{prettyName}/ranking")
    public ResponseEntity<?> listarRankingByEvento(@PathVariable String prettyName){
        try{
            return ResponseEntity.ok(servico.listarRankingCompleto(prettyName).subList(0, 3)); //se conseguir gerar a tabela de ranking, retorna a lista do ranking
        } catch (EventoNotFoundException ex){
            return ResponseEntity.status(404).body(new MensagemErro(ex.getMessage()));
        }
    }

    @GetMapping("/inscricao/{prettyName}/ranking/{usuarioId}")
    public ResponseEntity<?> listarRankingByEventoAndUsuario(@PathVariable String prettyName,
                                                             @PathVariable Long usuarioId){
        try{
            return ResponseEntity.ok(servico.getRankingByUser(prettyName, usuarioId));
        } catch (Exception ex){
            return ResponseEntity.status(404).body(new MensagemErro(ex.getMessage()));
        }
    }
}
