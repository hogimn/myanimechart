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
public class AnimeListStatusDto {
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

    public static AnimeListStatusDto from(AnimeListStatus animeListStatus) {
        AnimeListStatusDto animeListStatusDto = new AnimeListStatusDto();
        animeListStatusDto.setAnimeId(animeListStatus.getAnime().getID());
        animeListStatusDto.setStatus(animeListStatus.getStatus().field());
        animeListStatusDto.setScore(animeListStatus.getScore());
        animeListStatusDto.setStartDate(animeListStatus.getStartDate());
        animeListStatusDto.setFinishDate(animeListStatus.getFinishDate());
        animeListStatusDto.setPriority(animeListStatus.getPriority().value());
        animeListStatusDto.setTags(List.of(animeListStatus.getTags()));
        animeListStatusDto.setComments(animeListStatus.getComments());
        animeListStatusDto.setUpdatedAt(animeListStatus.getUpdatedAt());
        animeListStatusDto.setWatchedEpisodes(animeListStatus.getWatchedEpisodes());
        animeListStatusDto.setRewatching(animeListStatus.isRewatching());
        animeListStatusDto.setTimesRewatched(animeListStatus.getTimesRewatched());
        animeListStatusDto.setRewatchValue(animeListStatus.getRewatchValue().value());
        return animeListStatusDto;
    }
}
