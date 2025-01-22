package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.AnimeStatDao;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.dto.AnimeStatDto;
import com.hogimn.myanimechart.database.anime.repository.AnimeStatRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnimeStatService {
    private final AnimeStatRepository animeStatRepository;
    private final AnimeService animeService;

    public AnimeStatService(AnimeStatRepository animeStatRepository, AnimeService animeService) {
        this.animeStatRepository = animeStatRepository;
        this.animeService = animeService;
    }

    @Transactional
    public void saveAnimeStat(AnimeStatDto animeStatDto) {
        if (animeStatDto == null) {
            return;
        }

        AnimeDao animeDao = animeService.getAnimeDaoById(animeStatDto.getAnimeId());
        AnimeStatDao animeStatDao = new AnimeStatDao();
        animeStatDao.setAnime(animeDao);
        animeStatDao.setMembers(animeStatDto.getMembers());
        animeStatDao.setScore(animeStatDto.getScore());
        animeStatDao.setRecordedAt(animeStatDto.getRecordedAt());
        animeStatDao.setPopularity(animeStatDto.getPopularity());
        animeStatDao.setScoringCount(animeStatDto.getScoringCount());
        animeStatDao.setRank(animeStatDto.getRank());
        animeStatDto.setRecordedAt(DateUtil.now());

        animeStatRepository.save(animeStatDao);
    }

    public List<AnimeStatDao> getAnimeStatDaosByAnime(AnimeDao animeDao) {
        return animeStatRepository.findByAnime(animeDao);
    }

    public List<AnimeStatDto> getAnimeStatDtosByAnime(AnimeDao animeDao) {
        List<AnimeStatDao> animeStatDaos = getAnimeStatDaosByAnime(animeDao);
        return animeStatDaos.stream().map(AnimeStatDto::from).toList();
    }

    public List<AnimeDto> getAnimeStats(Integer year, String season) {
        List<AnimeDao> animeDaos = animeService.getAnimeDaosByYearAndSeason(year, season);
        List<AnimeDto> animeDtos = animeDaos.stream().map(AnimeDto::from).toList();
        for (int i = 0; i < animeDtos.size(); i++) {
            AnimeDao animeDao = animeDaos.get(i);
            AnimeDto animeDto = animeDtos.get(i);
            List<AnimeStatDto> animeStat = getAnimeStatDtosByAnime(animeDao);
            animeDto.setAnimeStats(animeStat);
        }

        return animeDtos;
    }

    public AnimeDto getAnimeStatsByTitle(String title) {
        AnimeDao animeDao = animeService.getAnimeDaoByTitle(title);
        AnimeDto animeDto = AnimeDto.from(animeDao);
        List<AnimeStatDto> animeStat = getAnimeStatDtosByAnime(animeDao);
        animeDto.setAnimeStats(animeStat);
        return animeDto;
    }

    public AnimeDto getAnimeStatsById(Long id) {
        AnimeDao animeDao = animeService.getAnimeDaoById(id);
        AnimeDto animeDto = AnimeDto.from(animeService.getAnimeDaoById(id));
        List<AnimeStatDto> animeStat = getAnimeStatDtosByAnime(animeDao);
        animeDto.setAnimeStats(animeStat);
        return animeDto;
    }

    public List<AnimeDto> getAnimeStatsByKeyword(String keyword) {
        List<AnimeDao> animeDaos = animeService.getAnimeDaosByKeyword(keyword);
        List<AnimeDto> animeDtos = animeService.getAnimeDtosByKeyword(keyword);
        for (int i = 0; i < animeDtos.size(); i++) {
            AnimeDao animeDao = animeDaos.get(i);
            AnimeDto animeDto = animeDtos.get(i);
            List<AnimeStatDto> animeStat = getAnimeStatDtosByAnime(animeDao);
            animeDto.setAnimeStats(animeStat);
        }
        return animeDtos;
    }
}