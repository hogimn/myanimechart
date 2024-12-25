package com.hogimn.myanimechart.database.anime.dao;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.key.AnimeStatId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "anime_stat")
@Data
@IdClass(AnimeStatId.class)
public class AnimeStatDao {
    @Id
    @ManyToOne
    @JoinColumn(name = "anime_id", referencedColumnName = "id")
    private AnimeDao anime;

    private int members;
    private double score;
    private Integer popularity;
    private Integer scoringCount;
    private Integer episodes;

    @Id
    private LocalDateTime recordedAt;

    public static AnimeStatDao from(AnimeDao anime) {
        AnimeStatDao animeDao = new AnimeStatDao();
        animeDao.anime = anime;
        animeDao.members = anime.getMembers();
        animeDao.score = anime.getScore();
        animeDao.recordedAt = DateUtil.now();
        animeDao.popularity = anime.getPopularity();
        animeDao.scoringCount = anime.getScoringCount();
        animeDao.episodes = anime.getEpisodes();
        return animeDao;
    }
}
