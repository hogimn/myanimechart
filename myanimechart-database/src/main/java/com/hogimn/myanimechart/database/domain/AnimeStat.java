package com.hogimn.myanimechart.database.domain;

import com.hogimn.myanimechart.database.dao.AnimeStatDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeStat {
    private Long id;
    private Double score;
    private Integer members;

    public static AnimeStat from(AnimeStatDao animeStatDao) {
        AnimeStat animeStat = new AnimeStat();
        animeStat.setId(animeStatDao.getId());
        animeStat.setScore(animeStatDao.getScore());
        animeStat.setMembers(animeStatDao.getMembers());
        return animeStat;
    }
}
