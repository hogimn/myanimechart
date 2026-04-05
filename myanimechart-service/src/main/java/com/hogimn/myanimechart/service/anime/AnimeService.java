package com.hogimn.myanimechart.service.anime;

import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.common.util.DateUtil;
import com.hogimn.myanimechart.core.domain.anime.AnimeAirStatusCode;
import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;
import com.hogimn.myanimechart.core.domain.anime.AnimePollRow;
import com.hogimn.myanimechart.core.domain.anime.AnimeRepository;
import com.hogimn.myanimechart.core.domain.poll.PollEntity;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.CollectionStatus;
import com.hogimn.myanimechart.service.exception.AnimeNotFoundException;
import com.hogimn.myanimechart.service.poll.PollResponse;
import com.hogimn.myanimechart.service.poll.PollService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final PollService pollService;

    @Transactional
    public SaveResult save(AnimeCreateRequest request) {
        Optional<AnimeEntity> optional = animeRepository.findById(request.id());
        if (optional.isPresent()) {
            AnimeEntity animeEntity = optional.get();
            animeEntity.update(request.toEntity());
            return SaveResult.UPDATED;
        }

        AnimeEntity animeEntity = request.toEntity();
        animeEntity.setCreatedAt(DateUtil.now());
        animeRepository.save(animeEntity);
        return SaveResult.CREATED;
    }

    public AnimeResponse getAnimeById(long id) {
        AnimeEntity animeEntity = animeRepository.findById(id)
                .orElseThrow(() -> new AnimeNotFoundException(id));
        return AnimeResponse.from(animeEntity);
    }

    public List<AnimeResponse> getAnimesByYearAndSeasonOrderByScore(int year, String season) {
        List<AnimeEntity> result = animeRepository.findByYearAndSeasonOrderByScoreDesc(year, season);
        return result.stream()
                .map(AnimeResponse::from)
                .toList();
    }

    public List<AnimeResponse> getFailedCollectionAnimesByYearAndSeason(int year, String season) {
        List<AnimeEntity> result = animeRepository.findByYearAndSeasonAndCollectStatusFailedOrderByScoreDesc(
                year,
                season,
                CollectionStatus.FAILED
        );
        return result.stream()
                .map(AnimeResponse::from)
                .toList();
    }

    public List<AnimeResponse> getOldSeasonCurrentlyAiringAnimes(
            int year,
            String season,
            int nextYear,
            String nextSeason) {
        List<AnimeEntity> result = animeRepository.findAnimeEntitiesOldSeasonCurrentlyAiring(
                year,
                season,
                nextYear,
                nextSeason,
                AnimeAirStatusCode.CURRENTLY_AIRING.code(),
                AnimeAirStatusCode.FINISHED_AIRING.code());
        return result.stream()
                .map(AnimeResponse::from)
                .toList();
    }

    public List<AnimeResponse> getCurrentAiringAnimes() {
        List<AnimeEntity> result = animeRepository.findAnimeEntitiesAllSeasonCurrentlyAiring(
                AnimeAirStatusCode.CURRENTLY_AIRING.code(),
                AnimeAirStatusCode.FINISHED_AIRING.code());
        return result.stream()
                .map(AnimeResponse::from)
                .toList();
    }

    public List<AnimeResponse> getByYearAndSeason(int year, String season) {
        List<AnimePollRow> results = animeRepository.findWithPollsByYearAndSeason(year, season);
        return mapToAnimeResponses(results);
    }

    public List<AnimeResponse> getByKeyword(String keyword) {
        List<AnimePollRow> results = animeRepository.findAllWithPollsByTitleContaining(keyword);
        return mapToAnimeResponses(results);
    }

    private List<AnimeResponse> mapToAnimeResponses(List<AnimePollRow> results) {
        Map<Long, AnimeEntity> animeMap = new LinkedHashMap<>();
        Map<Long, List<PollResponse>> pollListMap = new HashMap<>();

        for (AnimePollRow row : results) {
            AnimeEntity animeEntity = row.anime();
            PollEntity pollEntity = row.poll();

            long animeId = animeEntity.getId();
            animeMap.putIfAbsent(animeId, animeEntity);

            if (pollEntity != null) {
                pollListMap.computeIfAbsent(animeId, k -> new ArrayList<>())
                        .add(PollResponse.from(pollEntity));
            }
        }

        return animeMap.values().stream()
                .map(animeEntity -> {
                    List<PollResponse> polls = pollListMap.getOrDefault(animeEntity.getId(), List.of());
                    List<PollResponse> filteredPolls = polls.isEmpty()
                            ? polls
                            : pollService.filterByMaxTopicVotes(polls);
                    return AnimeResponse.from(animeEntity, filteredPolls);
                })
                .toList();
    }

    public List<AnimeResponse> getFailedCollectionAnimes() {
        List<AnimeEntity> result = animeRepository.findAnimesByCollectionStatus(CollectionStatus.FAILED);
        return result.stream()
                .map(AnimeResponse::from)
                .toList();
    }

    public List<AnimeResponse> getAnimesWithEmptyPoll() {
        List<AnimeEntity> result = animeRepository.findAnimesWithEmptyPoll();
        return result.stream()
                .map(AnimeResponse::from)
                .toList();
    }

    public List<AnimeResponse> getAllAnimes() {
        List<AnimeEntity> result = animeRepository.findAll();
        return result.stream()
                .map(AnimeResponse::from)
                .toList();
    }
}
