package com.hogimn.myanimechart.service.poll.status;

import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusEntity;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class MalPollCollectionStatusService {
    private final PollCollectionStatusRepository pollCollectionStatusRepository;

    public MalPollCollectionStatusService(
            PollCollectionStatusRepository pollCollectionStatusRepository
    ) {
        this.pollCollectionStatusRepository = pollCollectionStatusRepository;
    }

    public PollCollectionStatusEntity findPollCollectionStatusEntityByAnimeId(long animeId) {
        return pollCollectionStatusRepository.findById(animeId).orElse(null);
    }

    public void save(PollCollectionStatusDto pollCollectionStatus) {
        PollCollectionStatusEntity pollCollectionStatusEntity =
                findPollCollectionStatusEntityByAnimeId(
                        pollCollectionStatus.getAnimeId());

        if (pollCollectionStatusEntity == null) {
            pollCollectionStatusEntity = new PollCollectionStatusEntity();
        }

        pollCollectionStatusEntity.setAnimeId(pollCollectionStatus.getAnimeId());
        pollCollectionStatusEntity.setStatus(pollCollectionStatus.getStatus());
        pollCollectionStatusEntity.setUpdatedAt(pollCollectionStatus.getUpdatedAt());
        pollCollectionStatusEntity.setFinishedAt(pollCollectionStatus.getFinishedAt());
        pollCollectionStatusEntity.setStartedAt(pollCollectionStatus.getStartedAt());
        save(pollCollectionStatusEntity);
    }

    private void save(PollCollectionStatusEntity pollCollectionStatusEntity) {
        pollCollectionStatusRepository.save(pollCollectionStatusEntity);
    }
}
