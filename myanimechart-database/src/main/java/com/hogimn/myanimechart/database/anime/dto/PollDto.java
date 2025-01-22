package com.hogimn.myanimechart.database.anime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollDto {
    private Long animeId;
    private Integer pollOptionId;
    private Long topicId;
    private Integer episode;
    private String title;
    private Integer votes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
