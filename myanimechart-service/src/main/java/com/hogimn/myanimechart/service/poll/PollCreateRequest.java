package com.hogimn.myanimechart.service.poll;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PollCreateRequest {
    @NotBlank(message = "animeId is required.")
    private Long animeId;
    @NotBlank(message = "pollOptionId is required.")
    private Integer pollOptionId;
    @NotBlank(message = "topicId is required.")
    private Long topicId;
    private Integer episode;
    private String title;
    private Integer votes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
