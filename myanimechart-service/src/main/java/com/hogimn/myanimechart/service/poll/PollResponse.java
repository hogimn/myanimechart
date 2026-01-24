package com.hogimn.myanimechart.service.poll;

import com.hogimn.myanimechart.core.domain.poll.PollEntity;

public record PollResponse(
        Long animeId,
        Integer pollOptionId,
        Long topicId,
        Integer episode,
        String title,
        Integer votes
) {
    public static PollResponse from(PollEntity entity) {
        return new PollResponse(
                entity.getAnimeId(),
                entity.getPollOptionId(),
                entity.getTopicId(),
                entity.getEpisode(),
                entity.getTitle(),
                entity.getVotes()
        );
    }
}
