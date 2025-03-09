package com.hogimn.myanimechart.common.collect;

import com.hogimn.myanimechart.common.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.List;

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
