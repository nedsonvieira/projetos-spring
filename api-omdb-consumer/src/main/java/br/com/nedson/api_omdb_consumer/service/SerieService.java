package br.com.nedson.api_omdb_consumer.service;

import br.com.nedson.api_omdb_consumer.dto.EpisodioDTO;
import br.com.nedson.api_omdb_consumer.dto.SerieDTO;
import br.com.nedson.api_omdb_consumer.model.Categoria;
import br.com.nedson.api_omdb_consumer.model.Serie;
import br.com.nedson.api_omdb_consumer.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;

    public List<SerieDTO> obterTodasSeries(){
        return converteDados(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositorio.encontrarEpisodiosMaisRecentes());
    }

    private List<SerieDTO> converteDados(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDTO(
                        s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse()))
                .collect(Collectors.toList());
    }


    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(
                    s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporads(Long id) {
        Optional<Serie> serie = repositorio.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(
                            e.getTemporada(),
                            e.getNumeroEpisodio(),
                            e.getTitulo()
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumeros(Long id, Long numeroTemporada) {
        return repositorio.obterEpisodiosPorTemporada(id, numeroTemporada)
                .stream().map(e -> new EpisodioDTO(
                        e.getTemporada(),
                        e.getNumeroEpisodio(),
                        e.getTitulo()
                ))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriesPorCategoria(String nomeCategoria) {
        Categoria categoria = Categoria.fromPtBr(nomeCategoria);
        return converteDados(repositorio.findByGenero(categoria));
    }

    public List<EpisodioDTO> obterTop5EpsPorSerie(Long id) {
        return repositorio.obterTop5EpsPorSerie(id)
                .stream().map(e -> new EpisodioDTO(
                        e.getTemporada(),
                        e.getNumeroEpisodio(),
                        e.getTitulo()
                ))
                .collect(Collectors.toList());
    }
}
