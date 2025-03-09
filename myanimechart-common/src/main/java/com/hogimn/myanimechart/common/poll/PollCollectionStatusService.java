package com.hogimn.myanimechart.common.poll;

import com.hogimn.myanimechart.common.serviceregistry.RegisteredService;
import com.hogimn.myanimechart.common.serviceregistry.ServiceRegistryService;
import com.hogimn.myanimechart.common.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PollCollectionStatusService {
    private final PollCollectionStatusRepository pollCollectionStatusRepository;
    private final ServiceRegistryService serviceRegistryService;

    public PollCollectionStatusService(
            PollCollectionStatusRepository pollCollectionStatusRepository,
            ServiceRegistryService serviceRegistryService
    ) {
        this.pollCollectionStatusRepository = pollCollectionStatusRepository;
        this.serviceRegistryService = serviceRegistryService;
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

    public void setFailForWait() {
        List<PollCollectionStatusEntity> pollCollectionStatusEntities = pollCollectionStatusRepository
                .findByStatus(CollectionStatus.WAIT);

        for (PollCollectionStatusEntity pollCollectionStatusEntity : pollCollectionStatusEntities) {
            pollCollectionStatusEntity.setStatus(CollectionStatus.FAILED);
            pollCollectionStatusEntity.setUpdatedAt(DateUtil.now());
            pollCollectionStatusRepository.save(pollCollectionStatusEntity);
        }
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

    public void savePollCollectionStatusForFail(long animeId) {
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

        PollCollectionStatusDto pollCollectionStatusDto = PollCollectionStatusDto
                .from(pollCollectionStatusEntity);

        serviceRegistryService.send(
                RegisteredService.EXECUTE, "/poll/savePollCollectionStatus", pollCollectionStatusDto);
    }

    public void savePollCollectionStatusForEnd(long animeId) {
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

        PollCollectionStatusDto pollCollectionStatusDto = PollCollectionStatusDto
                .from(pollCollectionStatusEntity);

        serviceRegistryService.send(
                RegisteredService.EXECUTE, "/poll/savePollCollectionStatus", pollCollectionStatusDto);
    }

    public void savePollCollectionStatusForStart(long animeId) {
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

        PollCollectionStatusDto pollCollectionStatusDto = PollCollectionStatusDto
                .from(pollCollectionStatusEntity);

        serviceRegistryService.send(
                RegisteredService.EXECUTE, "/poll/savePollCollectionStatus", pollCollectionStatusDto);
    }

    public void savePollCollectionStatusForWait(long animeId) {
        PollCollectionStatusEntity pollCollectionStatusEntity =
                findPollCollectionStatusEntityByAnimeId(animeId);

        if (pollCollectionStatusEntity == null) {
            pollCollectionStatusEntity = new PollCollectionStatusEntity();
        }

        LocalDateTime now = DateUtil.now();
        pollCollectionStatusEntity.setAnimeId(animeId);
        pollCollectionStatusEntity.setStatus(CollectionStatus.WAIT);
        pollCollectionStatusEntity.setUpdatedAt(now);

        PollCollectionStatusDto pollCollectionStatusDto = PollCollectionStatusDto
                .from(pollCollectionStatusEntity);

        serviceRegistryService.send(
                RegisteredService.EXECUTE, "/poll/savePollCollectionStatus", pollCollectionStatusDto);
    }
}
