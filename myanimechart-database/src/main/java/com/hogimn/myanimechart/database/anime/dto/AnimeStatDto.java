package com.hogimn.myanimechart.database.anime.dto;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeStatDao;
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

    public static AnimeStatDto from(AnimeStatDao animeStatDao) {
        AnimeStatDto animeStat = new AnimeStatDto();
        animeStat.setAnimeId(animeStatDao.getAnime().getId());
        animeStat.setScore(animeStatDao.getScore());
        animeStat.setMembers(animeStatDao.getMembers());
        animeStat.setRecordedAt(animeStatDao.getRecordedAt());
        animeStat.setPopularity(animeStatDao.getPopularity());
        animeStat.setScoringCount(animeStatDao.getScoringCount());
        animeStat.setRank(animeStatDao.getRank());
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
        animeStatDto.setRecordedAt(DateUtil.now());
        return animeStatDto;
    }
}
