package com.movies.screenMatch.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesData(
       @JsonAlias("Title") String title,
       @JsonAlias("ImdbRating") String imdbRating,
       @JsonAlias("totalSeasons") String totalSeasons,
       @JsonAlias("Poster") String image) {

    @Override
    public String toString() {
        return "🎥 --- FICHA TÉCNICA --- 🎥\n" +
                "📌 Título: " + title + "\n" +
                "⭐ Avaliação IMDb: " + (imdbRating != null ? imdbRating + " estrelas" : "Sem nota ainda") + "\n" +
                "🎟️ Temporadas: " + (totalSeasons != null ? totalSeasons : "N/A") + "\n" +
                "🖼️ Pôster: " + (image != null ? image : "Imagem não disponível") + "\n" +
                "-----------------------------";
    }
}
