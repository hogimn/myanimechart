package com.hogimn.myanimechart.service.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AnimeListStatusRequest {
    @NotBlank(message = "animeId is required.")
    private Long animeId;
    private String status;
    private Integer score;
    private Date startDate;
    private Date finishDate;
    private Integer priority;
    private List<String> tags;
    private String comments;
    private Date updatedAt;
    private Integer watchedEpisodes;
    private Boolean rewatching;
    private Integer timesRewatched;
    private Integer rewatchValue;
}
