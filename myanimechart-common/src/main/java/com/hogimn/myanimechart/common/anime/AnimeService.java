package com.hogimn.myanimechart.common.anime;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.common.poll.PollDto;
import com.hogimn.myanimechart.common.poll.PollService;
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
    public void save(AnimeDto animeDto) {
        if (animeDto == null) {
            return;
        }

        Optional<AnimeEntity> optional = animeRepository.findById(animeDto.getId());
        if (optional.isPresent()) {
            AnimeEntity animeEntity = optional.get();
            animeEntity.setFrom(animeDto);
            animeEntity.setUpdatedAt(DateUtil.now());
            animeRepository.save(animeEntity);
            return;
        }

        AnimeEntity animeEntity = AnimeEntity.from(animeDto);
        animeEntity.setCreatedAt(DateUtil.now());
        animeRepository.save(animeEntity);
    }

    public AnimeDto findAnimeDtoById(long id) {
        return AnimeDto.from(findAnimeEntityById(id));
    }

    public AnimeEntity findAnimeEntityById(long id) {
        Optional<AnimeEntity> optional = animeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("Anime not found (" + id + ")");
    }

    public List<AnimeEntity> findAnimeEntitiesByYearAndSeasonOrderByScoreDesc(int year, String season) {
        return animeRepository.findByYearAndSeasonOrderByScoreDesc(year, season);
    }

    public List<AnimeEntity> findAnimeEntitiesOldSeasonCurrentlyAiring(int year, String season,
                                                                       int nextYear, String nextSeason) {
        return animeRepository.findAnimeEntitiesOldSeasonCurrentlyAiring(
                year, season, nextYear, nextSeason, "currently_airing", "finished_airing");
    }

    public List<AnimeEntity> findAnimeEntitiesAllSeasonCurrentlyAiring() {
        return animeRepository.findAnimeEntitiesAllSeasonCurrentlyAiring("currently_airing", "finished_airing");
    }

    public List<AnimeEntity> findAnimeEntitiesForceCollectTrue() {
        return animeRepository.findByForceCollect("Y");
    }

    public List<AnimeEntity> findAnimeEntitiesByKeyword(String keyword) {
        return animeRepository.findAllByTitleContaining(keyword);
    }

    public List<AnimeDto> findAnimeDtosWithPollDtosByKeyword(String keyword) {
        List<AnimeEntity> animeEntities = findAnimeEntitiesByKeyword(keyword);
        return convertToAnimeDtosWithPollDtos(animeEntities);
    }

    public List<AnimeDto> findAnimeDtosWithPollDtosByYearAndSeason(int year, String season) {
        List<AnimeEntity> animeEntities = findAnimeEntitiesByYearAndSeasonOrderByScoreDesc(year, season);
        return convertToAnimeDtosWithPollDtos(animeEntities);
    }

    private List<AnimeDto> convertToAnimeDtosWithPollDtos(List<AnimeEntity> animeEntities) {
        List<AnimeDto> animeDtos = animeEntities.stream()
                .map(AnimeDto::from)
                .toList();

        for (AnimeDto animeDto : animeDtos) {
            List<PollDto> pollDtos = pollService.findPollDtosByAnimeId(animeDto.getId());
            animeDto.setPolls(pollDtos);
        }

        return animeDtos;
    }
}
