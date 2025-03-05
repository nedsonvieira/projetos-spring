package br.com.nedson.AluraFlix.controller;

import br.com.nedson.AluraFlix.dto.video.DadosAtualizarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosBuscarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosCadastrarVideo;
import br.com.nedson.AluraFlix.dto.video.DadosDetalharVideo;
import br.com.nedson.AluraFlix.model.Video;
import br.com.nedson.AluraFlix.service.VideoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/videos")
@SecurityRequirement(name = "bearer-key")
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalharVideo> cadastrar(@RequestBody @Valid DadosCadastrarVideo dados){
        var video = videoService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosDetalharVideo(video));
    }

    @GetMapping
    public ResponseEntity<Page<DadosBuscarVideo>> listar(@PageableDefault(size = 5) Pageable paginacao){
        return ResponseEntity.status(HttpStatus.OK).body(
                videoService.listar(paginacao));
    }

    @GetMapping("/agrupado")
    public ResponseEntity<List<DadosBuscarVideo>> listarAgrupandoPorCategoria(){
        return ResponseEntity.status(HttpStatus.OK).body(
                videoService.listarAgrupandoPorCategoria());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalharVideo> listarById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                new DadosDetalharVideo(videoService.listarById(id)));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalharVideo> atualizar(@RequestBody @Valid DadosAtualizarVideo dados){
        return ResponseEntity.status(HttpStatus.OK).body(
                new DadosDetalharVideo(videoService.atualizar(dados)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable Long id){
        videoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Vídeo deletado com sucesso!");
    }
}
