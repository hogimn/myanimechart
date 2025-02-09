package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dto.AnimeDto;
import com.hogimn.myanimechart.database.anime.dto.AnimeStatDto;
import com.hogimn.myanimechart.database.anime.dto.PollDto;
import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import com.hogimn.myanimechart.database.anime.entity.AnimeStatEntity;
import com.hogimn.myanimechart.database.anime.repository.AnimeStatRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AnimeStatService {
    private final AnimeStatRepository animeStatRepository;
    private final AnimeService animeService;
    private final PollService pollService;

    public AnimeStatService(
            AnimeStatRepository animeStatRepository,
            AnimeService animeService,
            PollService pollService) {
        this.animeStatRepository = animeStatRepository;
        this.animeService = animeService;
        this.pollService = pollService;
    }

    @Transactional
    public void saveAnimeStat(AnimeStatDto animeStatDto) {
        if (animeStatDto == null) {
            return;
        }

        AnimeEntity animeEntity = animeService.getAnimeEntityByIdWithLock(animeStatDto.getAnimeId());
        AnimeStatEntity animeStatEntity = new AnimeStatEntity();
        animeStatEntity.setAnime(animeEntity);
        animeStatEntity.setMembers(animeStatDto.getMembers());
        animeStatEntity.setScore(animeStatDto.getScore());
        animeStatEntity.setRecordedAt(animeStatDto.getRecordedAt());
        animeStatEntity.setPopularity(animeStatDto.getPopularity());
        animeStatEntity.setScoringCount(animeStatDto.getScoringCount());
        animeStatEntity.setRank(animeStatDto.getRank());
        animeStatDto.setRecordedAt(DateUtil.now());

        animeStatRepository.save(animeStatEntity);
    }

    private List<AnimeStatEntity> getAnimeStatEntitiesByAnime(AnimeEntity animeEntity) {
        return animeStatRepository.findByAnimeOrderByRecordedAtAsc(animeEntity);
    }

    private List<AnimeStatDto> getAnimeStatDtosByAnime(AnimeEntity animeEntity) {
        List<AnimeStatEntity> animeStatEntities = getAnimeStatEntitiesByAnime(animeEntity);
        return animeStatEntities.stream().map(AnimeStatDto::from).toList();
    }

    public List<AnimeDto> getAnimeStatDtos(Integer year, String season) {
        List<AnimeEntity> animeEntities = animeService.getAnimeEntitiesByYearAndSeason(year, season).stream()
                .filter(e -> !Objects.equals(e.getHide(), "Y"))
                .toList();
        List<AnimeDto> animeDtos = animeEntities.stream()
                .map(AnimeDto::from)
                .toList();
        for (int i = 0; i < animeDtos.size(); i++) {
            AnimeEntity animeEntity = animeEntities.get(i);
            AnimeDto animeDto = animeDtos.get(i);
            List<AnimeStatDto> animeStat = getAnimeStatDtosByAnime(animeEntity);
            animeDto.setAnimeStats(animeStat);
            List<PollDto> pollDtos = pollService.getPollDtosByAnime(animeEntity);
            animeDto.setPolls(pollDtos);
        }

        return animeDtos;
    }

    public AnimeDto getAnimeStatDtoByTitle(String title) {
        AnimeEntity animeEntity = animeService.getAnimeEntityByTitle(title);
        AnimeDto animeDto = AnimeDto.from(animeEntity);
        List<AnimeStatDto> animeStat = getAnimeStatDtosByAnime(animeEntity);
        animeDto.setAnimeStats(animeStat);
        List<PollDto> pollDtos = pollService.getPollDtosByAnime(animeEntity);
        animeDto.setPolls(pollDtos);
        return animeDto;
    }

    public AnimeDto getAnimeStatDtoById(Long id) {
        AnimeEntity animeEntity = animeService.getAnimeEntityById(id);
        AnimeDto animeDto = AnimeDto.from(animeService.getAnimeEntityById(id));
        List<AnimeStatDto> animeStat = getAnimeStatDtosByAnime(animeEntity);
        animeDto.setAnimeStats(animeStat);
        List<PollDto> pollDtos = pollService.getPollDtosByAnime(animeEntity);
        animeDto.setPolls(pollDtos);
        return animeDto;
    }

    public List<AnimeDto> getAnimeStatDtoByKeyword(String keyword) {
        List<AnimeEntity> animeEntities = animeService.getAnimeEntitiesByKeyword(keyword);
        List<AnimeDto> animeDtos = animeService.getAnimeDtosByKeyword(keyword);
        for (int i = 0; i < animeDtos.size(); i++) {
            AnimeEntity animeEntity = animeEntities.get(i);
            AnimeDto animeDto = animeDtos.get(i);
            List<AnimeStatDto> animeStat = getAnimeStatDtosByAnime(animeEntity);
            animeDto.setAnimeStats(animeStat);
            List<PollDto> pollDtos = pollService.getPollDtosByAnime(animeEntity);
            animeDto.setPolls(pollDtos);
        }
        return animeDtos;
    }
}