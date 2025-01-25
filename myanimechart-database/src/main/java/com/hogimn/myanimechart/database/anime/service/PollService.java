package com.hogimn.myanimechart.database.anime.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.PollDao;
import com.hogimn.myanimechart.database.anime.dao.PollOptionDao;
import com.hogimn.myanimechart.database.anime.dto.PollDto;
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
        AnimeDao animeDao = animeService.getAnimeDaoByIdWithLock(pollDto.getAnimeId());
        PollOptionDao pollOptionDao = pollOptionService.getPollOptionDaoById(pollDto.getPollOptionId());
        Optional<PollDao> optional = pollRepository
                .findByAnimeAndPollOptionAndTopicId(animeDao, pollOptionDao, pollDto.getTopicId());
        LocalDateTime now = DateUtil.now();
        if (optional.isPresent()) {
            PollDao found = optional.get();
            found.setAnime(animeDao);
            found.setPollOption(pollOptionDao);
            found.setTopicId(pollDto.getTopicId());
            found.setTitle(pollDto.getTitle());
            found.setVotes(pollDto.getVotes());
            found.setUpdatedAt(now);
            found.setEpisode(pollDto.getEpisode());
            PollDao save = pollRepository.save(found);
            log.info("Updated existing poll: {}", save);
            return;
        }

        PollDao newPoll = new PollDao();
        newPoll.setAnime(animeDao);
        newPoll.setPollOption(pollOptionDao);
        newPoll.setTopicId(pollDto.getTopicId());
        newPoll.setTitle(pollDto.getTitle());
        newPoll.setVotes(pollDto.getVotes());
        newPoll.setCreatedAt(now);
        newPoll.setEpisode(pollDto.getEpisode());
        PollDao save = pollRepository.save(newPoll);
        log.info("Inserted new poll: {}", save);
    }

    private List<PollDao> getPollDaosByAnime(AnimeDao animeDao) {
        return pollRepository.findByAnimeOrderByEpisodeAscPollOptionAsc(animeDao);
    }

    public List<PollDto> getPollDtosByAnime(AnimeDao animeDao) {
        List<PollDao> pollDaos = getPollDaosByAnime(animeDao);
        return pollDaos.stream().map(PollDto::from).toList();
    }
}
