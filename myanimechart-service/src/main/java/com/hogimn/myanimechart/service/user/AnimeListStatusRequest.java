package com.hogimn.myanimechart.service.user;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record AnimeListStatusRequest(
        @NotNull(message = "animeId is required.")
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
}