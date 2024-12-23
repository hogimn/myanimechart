package com.hogimn.myanimechart.database.dao;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.dao.key.AnimeStatId;
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
    @Id
    private LocalDateTime recordedAt;

    public static AnimeStatDao from(AnimeDao anime) {
        AnimeStatDao animeDao = new AnimeStatDao();
        animeDao.anime = anime;
        animeDao.members = anime.getMembers();
        animeDao.score = anime.getScore();
        animeDao.recordedAt = DateUtil.now();
        return animeDao;
    }
}
