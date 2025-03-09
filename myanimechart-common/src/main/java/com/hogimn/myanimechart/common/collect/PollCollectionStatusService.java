package com.hogimn.myanimechart.common.collect;

import org.springframework.stereotype.Service;

@Service
public class PollCollectionStatusService {
    private final PollCollectionStatusRepository pollCollectionStatusRepository;

    public PollCollectionStatusService(PollCollectionStatusRepository pollCollectionStatusRepository) {
        this.pollCollectionStatusRepository = pollCollectionStatusRepository;
    }

    public void save(PollCollectionStatusEntity pollCollectionStatus) {
        pollCollectionStatusRepository.save(pollCollectionStatus);
    }

    public PollCollectionStatusEntity findPollCollectionStatusEntityByAnimeId(long animeId) {
        return pollCollectionStatusRepository.findById(animeId).orElse(null);
    }

    public void deleteAll() {
        pollCollectionStatusRepository.deleteAll();
    }
}
