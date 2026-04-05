package com.hogimn.myanimechart.service.poll;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record PollCreateRequest(
        @NotNull(message = "animeId is required.")
        Long animeId,

        @NotNull(message = "pollOptionId is required.")
        Integer pollOptionId,

        @NotNull(message = "topicId is required.")
        Long topicId,

        @NotNull(message = "episode is required.")
        Integer episode,

        @NotBlank(message = "title is required.")
        String title,

        @NotNull(message = "votes is required.")
        @PositiveOrZero(message = "votes must be zero or positive.")
        Integer votes,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
