package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemporada(@JsonAlias("Season") Integer numero,
                             @JsonAlias("Episodes") List<DadosEpisodio> episodios) {

    @Override
    public String toString() {
        return String.format("""
                        
                        \uD83C\uDF10 \u001B[34mTemporada %d\u001B[0m
                        \uD83D\uDCFA \u001B[36mEpisódios:\u001B[0m %d
                        %s
                        """,
                numero, episodios.size(), episodios);
    }
}