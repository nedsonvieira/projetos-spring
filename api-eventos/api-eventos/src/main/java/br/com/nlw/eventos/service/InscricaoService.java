package br.com.nlw.eventos.service;

import br.com.nlw.eventos.dto.RankingInscricoesPorUsuario;
import br.com.nlw.eventos.dto.ItemRankingInscricao;
import br.com.nlw.eventos.dto.InscricaoResponse;
import br.com.nlw.eventos.exception.EventoNotFoundException;
import br.com.nlw.eventos.exception.InscricaoConflictException;
import br.com.nlw.eventos.exception.UsuarioIndicadorNotFoundException;
import br.com.nlw.eventos.model.Evento;
import br.com.nlw.eventos.model.Inscricao;
import br.com.nlw.eventos.model.Usuario;
import br.com.nlw.eventos.repository.EventoRepository;
import br.com.nlw.eventos.repository.InscricaoRepository;
import br.com.nlw.eventos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class InscricaoService {

    @Autowired
    private EventoRepository eventoRepositorio;

    @Autowired
    private UsuarioRepository usuarioRepositorio;

    @Autowired
    private InscricaoRepository inscricaoRepositorio;

    public InscricaoResponse cadastrarInscricao(String eventoName, Usuario usuario, Long usuarioId){

        //recuperar o evento pelo nome
        Evento evento = eventoRepositorio.findByPrettyName(eventoName);
        if(evento == null){
            throw new EventoNotFoundException("Evento " + eventoName + " não existe!");
        }
        Usuario usuarioRecup = usuarioRepositorio.findByEmail(usuario.getEmail()); // user recuperado recebe um user ja cadastrado

        if(usuarioRecup == null){
            usuarioRecup = usuarioRepositorio.save(usuario); // grava no banco e reescreve no objeto user
        }
        Usuario indicador = null;
        // se o id do indicador existir, busca no repositorio, e se ele não existir cria uma inscrição no evento
        if(usuarioId != null) {
            indicador = usuarioRepositorio.findById(usuarioId).orElse(null);
            if (indicador == null) { // se buscar no repositorio e ele não existir, lança a exceção
                throw new UsuarioIndicadorNotFoundException("Usuário " + usuarioId + " indicador não existe!");
            }
        }
        //cria uma inscrição num evento
        Inscricao inscricao = new Inscricao();
        inscricao.setEvento(evento); // atribui o evento que foi recuperado na inscrição (user cadastrado mas não inscrito)
        inscricao.setInscrito(usuarioRecup); // atribui o users como sendo o subscriber
        inscricao.setIndicacao(indicador); //atribui ao indicador o userid de quem indicou

        Inscricao tempInsc = inscricaoRepositorio.findByEventoAndInscrito(evento, usuarioRecup);
        if (tempInsc != null){
            throw new InscricaoConflictException("Já existe inscrição para o usuário " + usuarioRecup.getNome() +
                    " no evento " + evento.getTitulo());
        }

        Inscricao result = inscricaoRepositorio.save(inscricao); // salva a inscrição no banco e retorna o id da inscrição e o link de indicação (designação)
        return new InscricaoResponse(result.getNumeroInscricao(), "http://codecraft.com/" + result.getEvento().getPrettyName() +
                "/" + result.getInscrito().getId());

    }
    //Retorna uma lista com o ranking indicações dos users em um evento
    public List<ItemRankingInscricao> listarRankingCompleto(String prettyName){
        Evento evt = eventoRepositorio.findByPrettyName(prettyName);
        if ((evt == null) || inscricaoRepositorio.gerarRanking(evt.getId()).isEmpty()){ //verifica se o evento não existe ou se existe e possui um ranking
            throw new EventoNotFoundException("Ranking do evento " + prettyName + " não existe!");
        }
        return inscricaoRepositorio.gerarRanking(evt.getId());
    }

    public RankingInscricoesPorUsuario getRankingByUser(String prettyName, Long usuarioId){
        List<ItemRankingInscricao> ranking = listarRankingCompleto(prettyName);

        //verifica a lista de users do ranking e verificar se possui um userId nela
        ItemRankingInscricao item = ranking.stream()
                .filter(i -> i.usuarioId().equals(usuarioId))
                .findFirst()
                .orElse(null);
        if(item == null){ // se não encontrar o userId na lista lança uma exceção
            throw new UsuarioIndicadorNotFoundException("Não há inscrições com indicação do usuário " + usuarioId);
        }
        Integer posicao = IntStream.range(0, ranking.size())//percorre cada um dos inteiros da pos 0 ate o tamanho da lista do ranking
                .filter(pos -> ranking.get(pos).usuarioId().equals(usuarioId))//a partir de cada pos ranking por userId, é verificado se é igual ao userId que é solicitado
                .findFirst()
                .getAsInt();
        //a posição na lista ranking começa em 0, soma +1 na posição para ter um ranking real
        return new RankingInscricoesPorUsuario(item, posicao+1);
    }

}
