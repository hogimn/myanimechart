package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dto.PollDto;
import com.hogimn.myanimechart.database.anime.entity.AnimeEntity;
import com.hogimn.myanimechart.database.anime.entity.PollEntity;
import com.hogimn.myanimechart.database.anime.entity.PollOptionEntity;
import com.hogimn.myanimechart.database.anime.repository.PollRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PollService {
    private final PollRepository pollRepository;
    private final AnimeService animeService;
    private final PollOptionService pollOptionService;

    public PollService(
            PollRepository pollRepository,
            AnimeService animeService,
            PollOptionService pollOptionService
    ) {
        this.pollRepository = pollRepository;
        this.animeService = animeService;
        this.pollOptionService = pollOptionService;
    }

    @Transactional
    public void upsertPoll(PollDto pollDto) {
        AnimeEntity animeEntity = animeService.getAnimeEntityByIdWithLock(pollDto.getAnimeId());
        PollOptionEntity pollOptionEntity = pollOptionService.getPollOptionEntityById(pollDto.getPollOptionId());
        Optional<PollEntity> optional = pollRepository
                .findByAnimeAndPollOptionAndTopicId(animeEntity, pollOptionEntity, pollDto.getTopicId());
        LocalDateTime now = DateUtil.now();
        if (optional.isPresent()) {
            PollEntity found = optional.get();
            found.setAnime(animeEntity);
            found.setPollOption(pollOptionEntity);
            found.setTopicId(pollDto.getTopicId());
            found.setTitle(pollDto.getTitle());
            found.setVotes(pollDto.getVotes());
            found.setUpdatedAt(now);
            found.setEpisode(pollDto.getEpisode());
            PollEntity save = pollRepository.save(found);
            log.info("Updated existing poll: {}", save);
            return;
        }

        PollEntity newPoll = new PollEntity();
        newPoll.setAnime(animeEntity);
        newPoll.setPollOption(pollOptionEntity);
        newPoll.setTopicId(pollDto.getTopicId());
        newPoll.setTitle(pollDto.getTitle());
        newPoll.setVotes(pollDto.getVotes());
        newPoll.setCreatedAt(now);
        newPoll.setEpisode(pollDto.getEpisode());
        PollEntity save = pollRepository.save(newPoll);
        log.info("Inserted new poll: {}", save);
    }

    private List<PollEntity> getPollEntitiesByAnime(AnimeEntity animeEntity) {
        return pollRepository.findByAnimeOrderByEpisodeAscPollOptionAsc(animeEntity);
    }

    public List<PollDto> getPollDtosByAnime(AnimeEntity animeEntity) {
        List<PollEntity> pollEntities = getPollEntitiesByAnime(animeEntity);
        return pollEntities.stream().map(PollDto::from).toList();
    }
}
