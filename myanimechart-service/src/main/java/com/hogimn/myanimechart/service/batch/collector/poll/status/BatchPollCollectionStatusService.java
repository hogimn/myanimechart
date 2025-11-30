package com.hogimn.myanimechart.service.batch.collector.poll.status;

import com.hogimn.myanimechart.core.domain.poll.collectionstatus.CollectionStatus;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusEntity;
import com.hogimn.myanimechart.core.domain.poll.collectionstatus.PollCollectionStatusRepository;
import com.hogimn.myanimechart.core.common.util.DateUtil;
import com.hogimn.myanimechart.service.anime.AnimeDto;
import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;
import com.hogimn.myanimechart.core.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.core.common.serviceregistry.ServiceRegistryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BatchPollCollectionStatusService {
    private final PollCollectionStatusRepository pollCollectionStatusRepository;
    private final ServiceRegistryService serviceRegistryService;

    public BatchPollCollectionStatusService(
            PollCollectionStatusRepository pollCollectionStatusRepository,
            ServiceRegistryService serviceRegistryService
    ) {
        this.pollCollectionStatusRepository = pollCollectionStatusRepository;
        this.serviceRegistryService = serviceRegistryService;
    }

    private PollCollectionStatusEntity findPollCollectionStatusEntityByAnimeId(long animeId) {
        return pollCollectionStatusRepository.findById(animeId).orElse(null);
    }

    @Transactional
    public void setFailForWait() {
        List<PollCollectionStatusEntity> pollCollectionStatusEntities = pollCollectionStatusRepository
                .findByStatusWithLock(CollectionStatus.WAIT);

        for (PollCollectionStatusEntity pollCollectionStatusEntity : pollCollectionStatusEntities) {
            pollCollectionStatusEntity.setStatus(CollectionStatus.FAILED);
            pollCollectionStatusEntity.setUpdatedAt(DateUtil.now());
            save(pollCollectionStatusEntity);
        }
    }

    @Transactional
    public void setFailForStartedButNotFinished() {
        List<PollCollectionStatusEntity> pollCollectionStatusEntities = pollCollectionStatusRepository
                .findByStatusAndFinishedAtIsNullWithLock(CollectionStatus.IN_PROGRESS);

        for (PollCollectionStatusEntity pollCollectionStatusEntity : pollCollectionStatusEntities) {
            pollCollectionStatusEntity.setStatus(CollectionStatus.FAILED);
            pollCollectionStatusEntity.setUpdatedAt(DateUtil.now());
            save(pollCollectionStatusEntity);
        }
    }

    public void sendSavePollCollectionStatusForFail(long animeId) {
        PollCollectionStatusEntity pollCollectionStatusEntity =
                findPollCollectionStatusEntityByAnimeId(animeId);

        if (pollCollectionStatusEntity == null) {
            pollCollectionStatusEntity = new PollCollectionStatusEntity();
        }

        LocalDateTime now = DateUtil.now();
        pollCollectionStatusEntity.setAnimeId(animeId);
        pollCollectionStatusEntity.setStatus(CollectionStatus.FAILED);
        pollCollectionStatusEntity.setFinishedAt(now);
        pollCollectionStatusEntity.setUpdatedAt(now);
        sendSave(pollCollectionStatusEntity);
    }

    public void sendSavePollCollectionStatusForEnd(long animeId) {
        PollCollectionStatusEntity pollCollectionStatusEntity =
                findPollCollectionStatusEntityByAnimeId(animeId);

        if (pollCollectionStatusEntity == null) {
            pollCollectionStatusEntity = new PollCollectionStatusEntity();
        }

        LocalDateTime now = DateUtil.now();
        pollCollectionStatusEntity.setAnimeId(animeId);
        pollCollectionStatusEntity.setStatus(CollectionStatus.COMPLETED);
        pollCollectionStatusEntity.setFinishedAt(now);
        pollCollectionStatusEntity.setUpdatedAt(now);
        sendSave(pollCollectionStatusEntity);
    }

    public void sendSavePollCollectionStatusForStart(long animeId) {
        PollCollectionStatusEntity pollCollectionStatusEntity =
                findPollCollectionStatusEntityByAnimeId(animeId);

        if (pollCollectionStatusEntity == null) {
            pollCollectionStatusEntity = new PollCollectionStatusEntity();
        }

        LocalDateTime now = DateUtil.now();
        pollCollectionStatusEntity.setAnimeId(animeId);
        pollCollectionStatusEntity.setStatus(CollectionStatus.IN_PROGRESS);
        pollCollectionStatusEntity.setStartedAt(now);
        pollCollectionStatusEntity.setFinishedAt(null);
        pollCollectionStatusEntity.setUpdatedAt(now);
        sendSave(pollCollectionStatusEntity);
    }

    public void sendSavePollCollectionStatusForWait(long animeId) {
        PollCollectionStatusEntity pollCollectionStatusEntity =
                findPollCollectionStatusEntityByAnimeId(animeId);

        if (pollCollectionStatusEntity == null) {
            pollCollectionStatusEntity = new PollCollectionStatusEntity();
        }

        LocalDateTime now = DateUtil.now();
        pollCollectionStatusEntity.setAnimeId(animeId);
        pollCollectionStatusEntity.setStatus(CollectionStatus.WAIT);
        pollCollectionStatusEntity.setUpdatedAt(now);
        sendSave(pollCollectionStatusEntity);
    }

    public void save(PollCollectionStatusEntity pollCollectionStatusEntity) {
        pollCollectionStatusRepository.save(pollCollectionStatusEntity);
    }

    private void sendSave(PollCollectionStatusEntity pollCollectionStatusEntity) {
        PollCollectionStatusDto pollCollectionStatusDto = PollCollectionStatusDto
                .from(pollCollectionStatusEntity);

        serviceRegistryService.send(
                RegisteredService.APPLICATION, "/poll/save-collection-status", pollCollectionStatusDto);
    }

    public List<PollCollectionStatusDto> getStatuses() {
        return pollCollectionStatusRepository
                .findAllWithAnimeOrderByYearAndSeasonAndScore()
                .stream()
                .map(objectList -> {
                    PollCollectionStatusEntity pollCollectionStatusEntity =
                            (PollCollectionStatusEntity) objectList[0];
                    AnimeEntity animeEntity = (AnimeEntity) objectList[1];
                    PollCollectionStatusDto pollCollectionStatusDto =
                            PollCollectionStatusDto.from(pollCollectionStatusEntity);
                    pollCollectionStatusDto.setAnimeDto(AnimeDto.from(animeEntity));
                    return pollCollectionStatusDto;
                })
                .toList();
    }
}
