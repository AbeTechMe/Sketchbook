package com.movies.screenMatch.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(@JsonAlias("Title") String title,
                          @JsonAlias("Episode") Integer episode,
                          @JsonAlias("imdbRating") String imdbRating,
                          @JsonAlias("Released") String released,
                          @JsonAlias("Poster") String image
){

    public double getImdbRatingAsDouble() {
        try {
            return Double.parseDouble(imdbRating);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public LocalDate getReleased(){
        try{
            return LocalDate.parse(released);
        }
        catch(DateTimeException e){
            System.out.println(e.getMessage());
        return null;}
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return "📺 --- "+ (episode != null ? episode + "° " : "N/A") + title +" --- 📺" + "\n" +
                "📅 Lançamento: " + (getReleased() != null ? getReleased().format(formatter): "Data não disponível") +
                " IMDB:" + (imdbRating != null ? imdbRating + "⭐" : "Sem nota") + "\n" +
                "-----------------------------";
    }


}
