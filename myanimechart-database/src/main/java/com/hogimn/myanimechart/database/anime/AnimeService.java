package com.hogimn.myanimechart.database.anime;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.poll.PollDto;
import com.hogimn.myanimechart.database.poll.PollService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final PollService pollService;

    public AnimeService(
            AnimeRepository animeRepository,
            PollService pollService
    ) {
        this.animeRepository = animeRepository;
        this.pollService = pollService;
    }

    @Transactional
    public void upsertAnime(AnimeDto animeDto) {
        if (animeDto == null) {
            return;
        }

        Optional<AnimeEntity> optional = animeRepository.findById(animeDto.getId());
        if (optional.isPresent()) {
            AnimeEntity animeEntity = optional.get();
            animeEntity.setFrom(animeDto);
            animeEntity.setUpdatedAt(DateUtil.now());
            AnimeEntity saved = animeRepository.save(animeEntity);
            log.info("Updated anime: {}", saved);
            return;
        }

        AnimeEntity animeEntity = AnimeEntity.from(animeDto);
        animeEntity.setCreatedAt(DateUtil.now());
        AnimeEntity saved = animeRepository.save(animeEntity);
        log.info("Inserted new anime: {}", saved);
    }

    public AnimeEntity getAnimeEntityById(Long id) {
        Optional<AnimeEntity> optional = animeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("Anime not found (" + id + ")");
    }

    public List<AnimeEntity> getAnimeEntitiesByYearAndSeason(int year, String season) {
        return animeRepository.findByYearAndSeason(year, season);
    }

    public List<AnimeEntity> getAnimeEntitiesOldSeasonCurrentlyAiring(int year, String season,
                                                                      int nextYear, String nextSeason) {
        return animeRepository.findAnimeEntitiesOldSeasonCurrentlyAiring(
                year, season, nextYear, nextSeason, "currently_airing", "finished_airing");
    }

    public List<AnimeEntity> getAnimeEntitiesAllSeasonCurrentlyAiring() {
        return animeRepository.findAnimeEntitiesAllSeasonCurrentlyAiring("currently_airing", "finished_airing");
    }

    public List<AnimeEntity> getAnimeEntitiesForceCollectTrue() {
        return animeRepository.findByForceCollect("Y");
    }

    public List<AnimeEntity> getAnimeEntitiesByKeyword(String keyword) {
        return animeRepository.findAllByTitleContaining(keyword);
    }

    public List<AnimeDto> getAnimeDtosWithPollByKeyword(String keyword) {
        List<AnimeEntity> animeEntities = getAnimeEntitiesByKeyword(keyword);
        return convertToAnimeDtoWithPolls(animeEntities);
    }

    public List<AnimeDto> getAnimeDtosWithPollByYearAndSeason(int year, String season) {
        List<AnimeEntity> animeEntities = getAnimeEntitiesByYearAndSeason(year, season);
        return convertToAnimeDtoWithPolls(animeEntities);
    }

    private List<AnimeDto> convertToAnimeDtoWithPolls(List<AnimeEntity> animeEntities) {
        List<AnimeDto> animeDtos = animeEntities.stream()
                .map(AnimeDto::from)
                .toList();

        for (int i = 0; i < animeDtos.size(); i++) {
            AnimeEntity animeEntity = animeEntities.get(i);
            AnimeDto animeDto = animeDtos.get(i);
            List<PollDto> pollDtos = pollService.getPollDtosByAnime(animeEntity);
            animeDto.setPolls(pollDtos);
        }

        return animeDtos;
    }
}
