package com.hogimn.myanimechart.service.poll;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PollCreateRequest(
        @NotNull(message = "animeId is required.")
        Long animeId,

        @NotNull(message = "pollOptionId is required.")
        Integer pollOptionId,

        @NotNull(message = "topicId is required.")
        Long topicId,

        Integer episode,
        String title,
        Integer votes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
