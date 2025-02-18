package com.movies.screenMatch.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonData(
        @JsonAlias("Season") Integer Season,
        @JsonAlias("Episodes")List<EpisodeData> episodes
) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("📅 --- TEMPORADA ").append(Season != null ? Season : "N/A").append(" --- 📅\n");

        if (episodes != null && !episodes.isEmpty()) {
            for (EpisodeData episode : episodes) {
                sb.append(episode).append("\n");
            }
        } else {
            sb.append("📭 Nenhum episódio disponível\n");
        }

        sb.append("-----------------------------");
        return sb.toString();
    }

}
