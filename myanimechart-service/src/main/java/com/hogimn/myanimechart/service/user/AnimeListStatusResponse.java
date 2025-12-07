package com.hogimn.myanimechart.service.user;

import dev.katsute.mal4j.anime.AnimeListStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeListStatusResponse {
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

    public static AnimeListStatusResponse from(AnimeListStatus animeListStatus) {
        AnimeListStatusResponse animeListStatusResponse = new AnimeListStatusResponse();
        animeListStatusResponse.setAnimeId(animeListStatus.getAnime().getID());
        animeListStatusResponse.setStatus(animeListStatus.getStatus().field());
        animeListStatusResponse.setScore(animeListStatus.getScore());
        animeListStatusResponse.setStartDate(animeListStatus.getStartDate());
        animeListStatusResponse.setFinishDate(animeListStatus.getFinishDate());
        animeListStatusResponse.setPriority(animeListStatus.getPriority().value());
        animeListStatusResponse.setTags(List.of(animeListStatus.getTags()));
        animeListStatusResponse.setComments(animeListStatus.getComments());
        animeListStatusResponse.setUpdatedAt(animeListStatus.getUpdatedAt());
        animeListStatusResponse.setWatchedEpisodes(animeListStatus.getWatchedEpisodes());
        animeListStatusResponse.setRewatching(animeListStatus.isRewatching());
        animeListStatusResponse.setTimesRewatched(animeListStatus.getTimesRewatched());
        animeListStatusResponse.setRewatchValue(animeListStatus.getRewatchValue().value());
        return animeListStatusResponse;
    }
}
