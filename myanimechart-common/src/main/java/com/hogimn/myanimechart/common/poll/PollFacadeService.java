package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.common.anime.AnimeEntity;
import com.hogimn.myanimechart.common.anime.AnimeService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class PollFacadeService {
    private final PollService pollService;
    private final AnimeService animeService;
    private final PollOptionService pollOptionService;

    public PollFacadeService(
            PollService pollService,
            AnimeService animeService,
            PollOptionService pollOptionService
    ) {
        this.pollService = pollService;
        this.animeService = animeService;
        this.pollOptionService = pollOptionService;
    }

    @Transactional
    public void upsertPoll(PollDto pollDto) {
        AnimeEntity animeEntity = animeService.getAnimeEntityById(pollDto.getAnimeId());
        PollOptionEntity pollOptionEntity = pollOptionService.getPollOptionEntityById(pollDto.getPollOptionId());
        Optional<PollEntity> optional = pollService
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
            PollEntity save = pollService.save(found);
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
        PollEntity save = pollService.save(newPoll);
        log.info("Inserted new poll: {}", save);
    }
}