package br.com.nedson.AluraFlix.service;

import br.com.nedson.AluraFlix.dto.video.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosBuscarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosCadastrarVideo;
import br.com.nedson.AluraFlix.model.Categoria;
import br.com.nedson.AluraFlix.model.Video;
import br.com.nedson.AluraFlix.repository.CategoriaRepository;
import br.com.nedson.AluraFlix.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    private final CategoriaRepository categoriaRepository;

    public Video cadastrar(DadosCadastrarVideo dados) {
        verificarUrl(dados.url());

        Categoria categoria;
        if (dados.categoriaId() == null) {
            categoria = categoriaLivre();
        } else {
            categoria = categoriaRepository.findByIdAndAtivoTrue(dados.categoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada!"));
        }
        return videoRepository.save(new Video(dados, categoria));
    }

    public Page<DadosBuscarVideo> listar(Pageable paginacao){
        return videoRepository.findAllByAtivoTrue(paginacao)
                .map(DadosBuscarVideo::new);
    }

    public List<DadosBuscarVideo> listarAgrupandoPorCategoria() {
        return videoRepository.agruparPorCategoria()
                .stream()
                .map(DadosBuscarVideo::new)
                .toList();
    }

    public Video listarById(Long id){
        return verificarId(id);
    }

    public Video atualizar(DadosAtualizarVideo dados){
        verificarId(dados.id());

        var video = videoRepository.getReferenceById(dados.id());
        video.atualizar(dados);
        return video;
    }

    public void deletar(Long id){
        verificarId(id);
        var video = videoRepository.getReferenceById(id);
        video.inativar();
    }

    private Categoria categoriaLivre() {
        return categoriaRepository.findByIdAndAtivoTrue(1L)
                .orElseThrow(() -> new EntityNotFoundException("Categoria Livre não existe!"));
    }
    private void verificarUrl(String url){
        videoRepository.findByUrl(url)
                .ifPresent(video -> {
                    throw new DataIntegrityViolationException("Já existe um vídeo salvo com este url!");
                });
    }
    private Video verificarId(Long id){
        if(videoRepository.findByIdAndAtivoTrue(id).isEmpty()){
            throw new EntityNotFoundException("Vídeo não encontrado!");
        }
        return videoRepository.getReferenceById(id);
    }
}
