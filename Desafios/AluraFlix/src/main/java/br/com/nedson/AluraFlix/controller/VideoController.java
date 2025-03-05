package br.com.nedson.AluraFlix.controller;

import br.com.nedson.AluraFlix.dto.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.DadosBuscarVideo;
import br.com.nedson.AluraFlix.dto.DadosCadastrarVideo;
import br.com.nedson.AluraFlix.dto.DadosDetalharVideo;
import br.com.nedson.AluraFlix.model.Video;
import br.com.nedson.AluraFlix.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/videos")
public class VideoController {

    private final VideoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarVideo(@RequestBody @Valid DadosCadastrarVideo dados,
                                    UriComponentsBuilder uriBuilder){
        var video = new Video(dados);
        repository.save(video);

        var uri = uriBuilder.path("videos/{id}").buildAndExpand(video.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalharVideo(video));
    }

//    @PostMapping("/lista")
//    @Transactional
//    public void postAllVideo(@RequestBody @Valid List<DadosCadastrarVideo> listaDados,
//                                       UriComponentsBuilder uriBuilder){
//        repositorio.saveAll(new Video().converterLista(listaDados));
//    }

    @GetMapping
    public ResponseEntity<Page<DadosBuscarVideo>> listarTodos(@PageableDefault(size = 5) Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao)
                .map(DadosBuscarVideo::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarById(@PathVariable Long id){
        var video = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalharVideo(video));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> atualizar(@RequestBody @Valid DadosAtualizarVideo dados){
        var video = repository.getReferenceById(dados.id());
        video.atualizar(dados);

        return ResponseEntity.ok(new DadosDetalharVideo(video));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable Long id){

        if (repository.findAtivoById(id) == null){
            throw new EntityNotFoundException();
        } else {
            var video = repository.getReferenceById(id);
            video.inativar();
            return ResponseEntity.status(HttpStatus.OK).body("Vídeo deletado com sucesso!");

        }
    }
}
