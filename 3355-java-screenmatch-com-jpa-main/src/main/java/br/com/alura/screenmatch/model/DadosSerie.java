package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Year") String ano,
        @JsonAlias("Rated") String classificacao,
        @JsonAlias("Released") String dataLancamento,
        @JsonAlias("Runtime") String duracao,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Writer") String escritor,
        @JsonAlias("Actors") String atores,
        @JsonAlias("Plot") String sinopse,
        @JsonAlias("Language") String idioma,
        @JsonAlias("Country") String pais,
        @JsonAlias("Awards") String premios,
        @JsonAlias("Poster") String poster,
        @JsonAlias("imdbRating") String avaliacao,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("Ratings") List<Rating> ratings) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record  Rating(@JsonAlias("Value") String avaliacaoPublica){};

    // Retorna o primeiro valor de avaliação disponível em Ratings.
    public String getPrimeiraAvaliacao() {
        if (ratings != null && !ratings.isEmpty()) {
            return ratings.get(0).avaliacaoPublica();
        }
        return "Sem avaliação disponível";
    }

    @Override
    public String toString() {
        return String.format("""
                \uD83C\uDFAC \u001B[36m========== Detalhes da Série ========== \u001B[0m 

                \uD83D\uDCDA \u001B[34mTítulo:\u001B[0m %s
                📆 \u001B[32mAno:\u001B[0m %s
                🏆 \u001B[33mPrêmios:\u001B[0m %s
                \uD83D\uDCC5 \u001B[32mLançamento:\u001B[0m %s
                ⏳ \u001B[31mDuração:\u001B[0m %s
                🎭 \u001B[36mGênero:\u001B[0m %s
                📝 \u001B[35mEscritor:\u001B[0m %s
                🎭 \u001B[35mAtores:\u001B[0m %s
                \uD83D\uDCC8 \u001B[33mTemporadas:\u001B[0m %d
                ⭐ \u001B[35mAvaliação IMDb:\u001B[0m %s
                🎖️ \u001B[36mAvaliação Pública:\u001B[0m %s

                \uD83C\uDFA5 \u001B[36mSinopse:\u001B[0m 
                \"%s\"

                🌎 \u001B[33mIdioma:\u001B[0m %s | \uD83C\uDF0D País: %s

                🖼️ \u001B[34mPoster:\u001B[0m %s

                \uD83C\uDFAC \u001B[36m==================================== \u001B[0m 
                """,
                titulo, ano, premios, dataLancamento, duracao, genero, escritor, atores, totalTemporadas,
                avaliacao, getPrimeiraAvaliacao(), sinopse, idioma, pais, poster);
    }

}
