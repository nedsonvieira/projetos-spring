package br.com.nedson.AluraFlix.repository;

import br.com.nedson.AluraFlix.dto.video.DadosBuscarVideo;
import br.com.nedson.AluraFlix.model.Video;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Page<Video> findAllByAtivoTrue(Pageable paginacao);

    Optional<Video> findByIdAndAtivoTrue(Long id);

    Optional<Video> findByUrl(String url);

    Boolean existsByIdAndAtivoTrue(Long id);

    @Query("""
            SELECT v FROM Video v
            WHERE v.ativo = true
            ORDER BY v.categoria.id, v.titulo""")
    List<Video> agruparPorCategoria();
}
