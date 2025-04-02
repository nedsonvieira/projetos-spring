package br.com.nedson.api_omdb_consumer.principal;

import br.com.nedson.api_omdb_consumer.model.Episodio;
import br.com.nedson.api_omdb_consumer.model.Categoria;
import br.com.nedson.api_omdb_consumer.model.DadosSerie;
import br.com.nedson.api_omdb_consumer.model.DadosTemporada;
import br.com.nedson.api_omdb_consumer.model.Serie;
import br.com.nedson.api_omdb_consumer.repository.SerieRepository;
import br.com.nedson.api_omdb_consumer.service.ConexaoApi;
import br.com.nedson.api_omdb_consumer.service.ConversaoDados;

import java.util.*;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConexaoApi conexaoApi = new ConexaoApi();
    private ConversaoDados conversor = new ConversaoDados();

    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String TEMPORADA = "&season=";
    private final String API_KEY = System.getenv("OMDB_API_KEY");

    private SerieRepository repositorio;

    private List<Serie> listaSeries = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repositorio) {
    this.repositorio = repositorio;
    }

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar série por ator
                    6 - Top 5 Series
                    7 - Buscar séries por categoria
                    8 - Filtrar séries
                    9 - Busca Eps por trecho
                    10 - Busca Eps a partir de uma data
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpPorTrecho();
                    break;
                case 10:
                    buscarEpsDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }

    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();

        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = conexaoApi.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + TEMPORADA + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.converterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(dt -> dt.episodios().stream()
                            .map(de -> new Episodio(dt.numero(), de)))
                    .toList();
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }

    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para buscar: ");
        var nomeSerie = leitura.nextLine();

        var json = conexaoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.converterDados(json, DadosSerie.class);
        return dados;
    }

    private void listarSeriesBuscadas() {
        listaSeries = repositorio.findAll();
        listaSeries.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()){
            System.out.println("Dados da Série: \n" + serieBusca.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator para busca: ");
        var nomeAtor = leitura.nextLine();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, 8.0);
        System.out.println("Series em que " + nomeAtor + " atuou:");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + " - Avaliação " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s ->
                System.out.println(s.getTitulo() + " - Avaliação " + s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero? ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPtBr(nomeGenero);

        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria " + nomeGenero);

        seriesPorCategoria.forEach(s ->
                System.out.println(s.getTitulo() + " - Avaliação " + s.getAvaliacao()));
    }

    private void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpPorTrecho() {
        System.out.println("Digite o nome do episódio para busca: ");
        var trechoEp = leitura.nextLine();
        List<Episodio> epsEncontrados = repositorio.epsPorTrecho(trechoEp);
        epsEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }


    private void buscarEpsDepoisDeUmaData() {
        buscarSeriePorTitulo();
        Serie serie = serieBusca.get();

        if(serieBusca.isPresent()){
            System.out.println("Digite o ano limite de lançamento: ");
            var anoLancamento = leitura.nextLine();

            List<Episodio> epsAno = repositorio.epsPorSerieEAno(serie, anoLancamento);
            epsAno.forEach(System.out::println);
        }
    }

}
