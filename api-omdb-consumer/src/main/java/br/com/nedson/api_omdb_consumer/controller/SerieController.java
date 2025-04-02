package br.com.nedson.api_omdb_consumer.controller;

import br.com.nedson.api_omdb_consumer.dto.EpisodioDTO;
import br.com.nedson.api_omdb_consumer.dto.SerieDTO;
import br.com.nedson.api_omdb_consumer.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servico;

    @GetMapping
    public List<SerieDTO> obterSeries(){
        return servico.obterTodasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series(){
        return servico.obterTop5Series();
    }

    @GetMapping("lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return servico.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id){
        return servico.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return servico.obterTodasTemporads(id);
    }

    @GetMapping("/{id}/temporadas/{numeroTemporada}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id,
                                                      @PathVariable Long numeroTemporada){
    return servico.obterTemporadasPorNumeros(id, numeroTemporada);
    }

    @GetMapping("/categoria/{nomeGenero}")
    public List<SerieDTO> obterSeriesPorCategoria(@PathVariable String nomeGenero){
        return servico.obterSeriesPorCategoria(nomeGenero);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDTO> obterTop5EpsPorSerie(@PathVariable Long id){
        return servico.obterTop5EpsPorSerie(id);
    }

}
