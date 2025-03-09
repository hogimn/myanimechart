package com.hogimn.myanimechart.common.poll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PollRepository extends JpaRepository<PollEntity, Long> {
    Optional<PollEntity> findByAnimeIdAndPollOptionIdAndTopicId(long animeId, int pollOptionId, long topicId);

    List<PollEntity> findByAnimeIdOrderByEpisodeAscTopicIdAscPollOptionIdAsc(long animeId);
}