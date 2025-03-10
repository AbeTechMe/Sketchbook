package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<Serie> seriesBuscadas = new ArrayList<>();

    public void exibeMenu() {
        var menu = """
                 ✨ [36mBEM-VINDO AO SCREENMATCH[0m ✨
                \s
                 [32m1️⃣ - Buscar séries[0m
                 [32m2️⃣ - Buscar episódios[0m
                 [32m3️⃣ - Listar séries buscadas[0m
                 [31m0️⃣ - Sair[0m                                \s
                \s""";


        while (true) {
            System.out.println(menu);
            System.out.print("\uD83D\uDC49 Escolha uma opção: ");
            var opcao = leitura.nextInt();
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
                case 0:
                    System.out.println("\u001B[31m\uD83D\uDEAB Saindo... Até logo!\u001B[0m");
                    return;

                default:
                    System.out.println("\u001B[31m⚠️ Opção inválida! Tente novamente.\u001B[0m");
            }
        }

    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        System.out.println("\n\u2728 \u001B[35mSérie encontrada:\u001B[0m \n" + dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.print("\uD83D\uDCDA Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        seriesBuscadas.add(new Serie(dados));
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        System.out.println("\n\uD83C\uDF1F \u001B[33mBuscando episódios da série:\u001B[0m " + dadosSerie.titulo());

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(t -> System.out.println("\u001B[36m\u2728 Temporada " + t.numero() + "\u001B[0m\n" + t));
    }

    private void listarSeriesBuscadas(){
        if(!seriesBuscadas.isEmpty()){
            System.out.println(" \uD83C\uDFA5 Séries buscadas \uD83D\uDCC2: ");
            seriesBuscadas.stream()
                    .sorted(Comparator.comparing(Serie::getGenero))
                    .forEach(System.out::println);
        }
        else{
            System.out.println("\uD83D\uDE22 Você ainda não tem séries buscadas...");
        }
    }
}
