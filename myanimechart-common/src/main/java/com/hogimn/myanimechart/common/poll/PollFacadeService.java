package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class PollFacadeService {
    private final PollService pollService;

    public PollFacadeService(PollService pollService) {
        this.pollService = pollService;
    }

    @Transactional
    public void save(PollDto pollDto) {
        Optional<PollEntity> optional = pollService
                .findByAnimeIdAndPollOptionIdAndTopicId(
                        pollDto.getAnimeId(), pollDto.getPollOptionId(), pollDto.getTopicId());
        LocalDateTime now = DateUtil.now();
        if (optional.isPresent()) {
            PollEntity found = optional.get();
            found.setAnimeId(pollDto.getAnimeId());
            found.setPollOptionId(pollDto.getPollOptionId());
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
        newPoll.setAnimeId(pollDto.getAnimeId());
        newPoll.setPollOptionId(pollDto.getPollOptionId());
        newPoll.setTopicId(pollDto.getTopicId());
        newPoll.setTitle(pollDto.getTitle());
        newPoll.setVotes(pollDto.getVotes());
        newPoll.setCreatedAt(now);
        newPoll.setEpisode(pollDto.getEpisode());
        PollEntity save = pollService.save(newPoll);
        log.info("Inserted new poll: {}", save);
    }
}