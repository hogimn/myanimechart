package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.PollDao;
import com.hogimn.myanimechart.database.anime.dao.PollOptionDao;
import com.hogimn.myanimechart.database.anime.repository.PollRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class PollService {
    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public void upsertPoll(AnimeDao animeDao, PollOptionDao pollOptionDao, long topicId, String topicTitle, int votes, int episode) {
        Optional<PollDao> optional = pollRepository.findByAnimeAndPollOptionAndTopicId(animeDao, pollOptionDao, topicId);
        LocalDateTime now = DateUtil.now();
        if (optional.isPresent()) {
            PollDao found = optional.get();
            found.setAnime(animeDao);
            found.setPollOption(pollOptionDao);
            found.setTopicId(topicId);
            found.setTitle(topicTitle);
            found.setVotes(votes);
            found.setUpdatedAt(now);
            found.setEpisode(episode);
            PollDao save = pollRepository.save(found);
            log.info("Updated existing poll: {}", save);
        } else {
            PollDao newPoll = new PollDao();
            newPoll.setAnime(animeDao);
            newPoll.setPollOption(pollOptionDao);
            newPoll.setTopicId(topicId);
            newPoll.setTitle(topicTitle);
            newPoll.setVotes(votes);
            newPoll.setCreatedAt(now);
            newPoll.setEpisode(episode);
            PollDao save = pollRepository.save(newPoll);
            log.info("Inserted new poll: {}", save);
        }
    }
}
