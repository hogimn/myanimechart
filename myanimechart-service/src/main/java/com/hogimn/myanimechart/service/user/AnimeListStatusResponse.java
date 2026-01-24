package com.hogimn.myanimechart.service.user;

import dev.katsute.mal4j.anime.AnimeListStatus;

import java.util.Date;
import java.util.List;

public record AnimeListStatusResponse(
        Long animeId,
        String status,
        Integer score,
        Date startDate,
        Date finishDate,
        Integer priority,
        List<String> tags,
        String comments,
        Date updatedAt,
        Integer watchedEpisodes,
        Boolean rewatching,
        Integer timesRewatched,
        Integer rewatchValue
) {
    public static AnimeListStatusResponse from(AnimeListStatus status) {
        return new AnimeListStatusResponse(
                status.getAnime().getID(),
                status.getStatus().field(),
                status.getScore(),
                status.getStartDate(),
                status.getFinishDate(),
                status.getPriority().value(),
                List.of(status.getTags()),
                status.getComments(),
                status.getUpdatedAt(),
                status.getWatchedEpisodes(),
                status.isRewatching(),
                status.getTimesRewatched(),
                status.getRewatchValue().value()
        );
    }
}
