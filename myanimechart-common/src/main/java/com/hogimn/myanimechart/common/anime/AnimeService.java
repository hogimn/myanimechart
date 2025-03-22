package com.hogimn.myanimechart.common.anime;

import com.hogimn.myanimechart.common.poll.PollEntity;
import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.common.poll.PollDto;
import com.hogimn.myanimechart.common.poll.PollService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public List<AnimeEntity> findAnimeEntitiesByYearAndSeasonAndCollectStatusFailedOrderByScoreDesc(int year, String season) {
        return animeRepository.findByYearAndSeasonAndCollectStatusFailedOrderByScoreDesc(year, season);
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

    public List<AnimeDto> findAnimeDtosWithPollDtosByYearAndSeason(int year, String season) {
        List<Object[]> results = animeRepository.findWithPollsByYearAndSeason(year, season);
        return convertToAnimeDtos(results);
    }

    public List<AnimeDto> findAnimeDtosWithPollDtosByKeyword(String keyword) {
        List<Object[]> results = animeRepository.findAllWithPollsByTitleContaining(keyword);
        return convertToAnimeDtos(results);
    }

    private List<AnimeDto> convertToAnimeDtos(List<Object[]> results) {
        Map<Long, AnimeDto> animeDtoMap = new HashMap<>();

        for (var result : results) {
            AnimeEntity animeEntity = (AnimeEntity) result[0];
            PollEntity pollEntity = (PollEntity) result[1];

            long animeId = animeEntity.getId();
            AnimeDto animeDto = animeDtoMap.computeIfAbsent(animeId,
                    id -> AnimeDto.from(animeEntity));

            if (animeDto.getPolls() == null) {
                animeDto.setPolls(new ArrayList<>());
            }

            if (pollEntity != null) {
                PollDto pollDto = PollDto.from(pollEntity);
                animeDto.getPolls().add(pollDto);
            }
        }

        return new ArrayList<>(animeDtoMap.values());
    }
}
