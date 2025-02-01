package com.hogimn.myanimechart.database.anime.dto;

import com.hogimn.myanimechart.database.anime.entity.AnimeStatEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeStatDto {
    private Long animeId;
    private Double score;
    private Integer members;
    private Integer popularity;
    private Integer scoringCount;
    private Integer rank;
    private LocalDateTime recordedAt;

    public static AnimeStatDto from(AnimeStatEntity animeStatEntity) {
        AnimeStatDto animeStat = new AnimeStatDto();
        animeStat.setAnimeId(animeStatEntity.getAnime().getId());
        animeStat.setScore(animeStatEntity.getScore());
        animeStat.setMembers(animeStatEntity.getMembers());
        animeStat.setRecordedAt(animeStatEntity.getRecordedAt());
        animeStat.setPopularity(animeStatEntity.getPopularity());
        animeStat.setScoringCount(animeStatEntity.getScoringCount());
        animeStat.setRank(animeStatEntity.getRank());
        return animeStat;
    }

    public static AnimeStatDto from(AnimeDto animeDto) {
        AnimeStatDto animeStatDto = new AnimeStatDto();
        animeStatDto.setAnimeId(animeDto.getId());
        animeStatDto.setScore(animeDto.getScore());
        animeStatDto.setRank(animeDto.getRank());
        animeStatDto.setPopularity(animeDto.getPopularity());
        animeStatDto.setScoringCount(animeDto.getScoringCount());
        animeStatDto.setMembers(animeDto.getMembers());
        return animeStatDto;
    }
}
