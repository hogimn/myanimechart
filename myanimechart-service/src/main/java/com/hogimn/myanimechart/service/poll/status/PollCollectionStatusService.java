package com.hogimn.myanimechart.service.poll.status;

import com.hogimn.myanimechart.core.common.result.SaveResult;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusEntity;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PollCollectionStatusService {
    private final PollCollectionStatusRepository pollCollectionStatusRepository;

    @Transactional
    public SaveResult save(PollCollectionStatusRequest request) {
        Optional<PollCollectionStatusEntity> optionalEntity = pollCollectionStatusRepository
                .findById(request.getAnimeId());

        final PollCollectionStatusEntity entity;
        final SaveResult result;

        if (optionalEntity.isPresent()) {
            entity = optionalEntity.get();
            result = SaveResult.UPDATED;
        } else {
            entity = new PollCollectionStatusEntity();
            result = SaveResult.CREATED;
        }
        entity.setAnimeId(request.getAnimeId());
        entity.setStatus(request.getStatus());
        entity.setUpdatedAt(request.getUpdatedAt());
        entity.setFinishedAt(request.getFinishedAt());
        entity.setStartedAt(request.getStartedAt());

        pollCollectionStatusRepository.save(entity);

        return result;
    }
}
