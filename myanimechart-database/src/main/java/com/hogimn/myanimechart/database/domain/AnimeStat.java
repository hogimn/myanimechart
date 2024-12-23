package com.hogimn.myanimechart.database.domain;

import com.hogimn.myanimechart.database.dao.AnimeStatDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeStat {
    private Long animeId;
    private Double score;
    private Integer members;

    public static AnimeStat from(AnimeStatDao animeStatDao) {
        AnimeStat animeStat = new AnimeStat();
        animeStat.setAnimeId(animeStatDao.getAnime().getId());
        animeStat.setScore(animeStatDao.getScore());
        animeStat.setMembers(animeStatDao.getMembers());
        return animeStat;
    }
}
