package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String titulo,
                            @JsonAlias("Episode") Integer numero,
                            @JsonAlias("imdbRating") String avaliacao,
                            @JsonAlias("Released") String dataLancamento) {

    @Override
    public String toString() {
        return String.format("""
                        
                        \uD83C\uDFAC \u001B[34mEpisódio %d:\u001B[0m ✨
                        \uD83D\uDCD6 \u001B[35mTítulo:\u001B[0m %s
                        ⭐ \u001B[33mAvaliação:\u001B[0m %s
                        \uD83D\uDCC5 \u001B[36mLançamento:\u001B[0m %s
                        """,
                numero, titulo, avaliacao, dataLancamento);
    }
}
