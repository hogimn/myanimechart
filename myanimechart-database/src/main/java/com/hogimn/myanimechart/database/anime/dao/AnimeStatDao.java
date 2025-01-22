package com.hogimn.myanimechart.database.anime.dao;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.key.AnimeStatId;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "anime_stat")
@Data
@IdClass(AnimeStatId.class)
public class
AnimeStatDao {
    @Id
    @ManyToOne
    @JoinColumn(name = "anime_id", referencedColumnName = "id")
    private AnimeDao anime;

    private int members;
    private double score;
    private Integer popularity;
    private Integer scoringCount;
    private Integer rank;

    @Id
    private LocalDateTime recordedAt;

    public static AnimeStatDao from(AnimeDao animeDao) {
        AnimeStatDao animeStatDao = new AnimeStatDao();
        animeStatDao.anime = animeDao;
        animeStatDao.members = animeDao.getMembers();
        animeStatDao.score = animeDao.getScore();
        animeStatDao.recordedAt = DateUtil.now();
        animeStatDao.popularity = animeDao.getPopularity();
        animeStatDao.scoringCount = animeDao.getScoringCount();
        animeStatDao.rank = animeDao.getRank();
        return animeStatDao;
    }
}
