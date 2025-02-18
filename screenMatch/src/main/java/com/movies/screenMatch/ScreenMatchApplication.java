package com.movies.screenMatch;

import com.movies.screenMatch.Model.EpisodeData;
import com.movies.screenMatch.Model.SeasonData;
import com.movies.screenMatch.Model.SeriesData;
import com.movies.screenMatch.Service.ConsumoApi;
import com.movies.screenMatch.Service.DataConvert;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Integer.parseInt;

@SpringBootApplication
public class ScreenMatchApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("call API 🎞");

		var api = new ConsumoApi();

		//series ----------------------------------------
		var json = api.obterDados("https://www.omdbapi.com/?t=sherlock&apikey=6585022c");
		var series = DataConvert.converter(json, SeriesData.class);
		System.out.println(series);

		//Seasons ----------------------------------------
		List<SeasonData> seasons = new ArrayList<>();
		for (int s = 1; s <= parseInt(series.totalSeasons()); s++) {
			json = api.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=" + s + "&apikey=6585022c");
			var season = DataConvert.converter(json, SeasonData.class);
			seasons.add(season);
		}

		// Five best ----------------------------------------

		System.out.println("**** TOP 5 EPISÓDIOS ****");
		List<EpisodeData>  episodes = seasons.stream()
				.flatMap( e -> e.episodes().stream())
				.toList(); //.collect(Collectors.toList()); // para fazer uma lista editavel

		episodes.stream()
				.filter(e -> !e.imdbRating().equalsIgnoreCase("N/A"))
				.sorted(Comparator.comparing(EpisodeData::imdbRating).reversed())
				.limit(5)
				.forEach(System.out::println);

		// Buscando episódio pela data
		System.out.println("Episódios a partir de 2004 > **************");
		episodes.stream()
				.filter(e -> e.getReleased() != null && e.getReleased().isAfter(LocalDate.of(2004,1,1)))
				.forEach(System.out::println);

	}
}
