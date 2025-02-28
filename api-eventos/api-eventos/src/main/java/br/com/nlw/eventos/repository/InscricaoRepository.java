package br.com.nlw.events.repository;

import br.com.nlw.events.dto.ItemRankingInscricao;
import br.com.nlw.events.model.Evento;
import br.com.nlw.events.model.Inscricao;
import br.com.nlw.events.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscricaoRepository extends CrudRepository<Inscricao, Long> {
    public Inscricao findByEventoAndInscrito(Evento evento, Usuario usuario);

    /*
    A anotação @Query cria uma consulta personalizada
    A consulta gera uma tabela com quantidade de inscrições por indicação de um user, o id e nome do user que indicou
     */
    @Query(value = "SELECT COUNT(numero_inscricao) AS quantidade, indicacao_usuario_id, nome" +
            "   FROM inscricoes INNER JOIN usuarios" +
            "   ON inscricoes.indicacao_usuario_id = usuarios.id" +
            "   WHERE indicacao_usuario_id IS NOT NULL" +
            "       AND evento_id = :eventoId" +
            "   GROUP BY indicacao_usuario_id" +
            "   ORDER BY quantidade DESC;", nativeQuery = true)
    public List<ItemRankingInscricao> gerarRanking(@Param("eventoId") Long eventoId);
}
