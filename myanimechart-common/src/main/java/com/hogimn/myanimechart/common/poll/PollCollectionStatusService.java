package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollCollectionStatusService {
    private final PollCollectionStatusRepository pollCollectionStatusRepository;

    public PollCollectionStatusService(PollCollectionStatusRepository pollCollectionStatusRepository) {
        this.pollCollectionStatusRepository = pollCollectionStatusRepository;
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

        pollCollectionStatusRepository.save(pollCollectionStatusEntity);
    }

    public void save(PollCollectionStatusEntity pollCollectionStatus) {
        pollCollectionStatusRepository.save(pollCollectionStatus);
    }

    public PollCollectionStatusEntity findPollCollectionStatusEntityByAnimeId(long animeId) {
        return pollCollectionStatusRepository.findById(animeId).orElse(null);
    }

    public void setFailForStartedButNotFinished() {
        List<PollCollectionStatusEntity> pollCollectionStatusEntities = pollCollectionStatusRepository
                .findByStatusAndFinishedAt(CollectionStatus.IN_PROGRESS, null);

        for (PollCollectionStatusEntity pollCollectionStatusEntity : pollCollectionStatusEntities) {
            pollCollectionStatusEntity.setStatus(CollectionStatus.FAILED);
            pollCollectionStatusEntity.setUpdatedAt(DateUtil.now());
            pollCollectionStatusRepository.save(pollCollectionStatusEntity);
        }
    }
}
