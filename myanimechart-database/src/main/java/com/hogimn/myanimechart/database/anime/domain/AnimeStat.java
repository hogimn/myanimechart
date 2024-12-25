package com.hogimn.myanimechart.database.anime.domain;

import com.hogimn.myanimechart.database.anime.dao.AnimeStatDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeStat {
    private Long animeId;
    private Double score;
    private Integer members;
    private LocalDateTime recordedAt;

    public static AnimeStat from(AnimeStatDao animeStatDao) {
        AnimeStat animeStat = new AnimeStat();
        animeStat.setAnimeId(animeStatDao.getAnime().getId());
        animeStat.setScore(animeStatDao.getScore());
        animeStat.setMembers(animeStatDao.getMembers());
        animeStat.setRecordedAt(animeStatDao.getRecordedAt());
        return animeStat;
    }
}
