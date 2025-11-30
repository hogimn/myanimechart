package com.hogimn.myanimechart.mal.poll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollRepository extends JpaRepository<PollEntity, Long> {
    Optional<PollEntity> findByAnimeIdAndPollOptionIdAndTopicId(long animeId, int pollOptionId, long topicId);
}