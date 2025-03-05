package br.com.nedson.AluraFlix.repository;

import br.com.nedson.AluraFlix.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Page<Video> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
           SELECT v.ativo FROM Video v
           WHERE v.id = :id""")
    Boolean findAtivoById(Long id);

}
