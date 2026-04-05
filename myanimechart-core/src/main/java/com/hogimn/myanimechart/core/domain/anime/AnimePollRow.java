package com.hogimn.myanimechart.core.domain.anime;

import com.hogimn.myanimechart.core.domain.poll.PollEntity;

/**
 * JPQL constructor projection for {@code SELECT a, b} anime/poll join queries.
 */
public record AnimePollRow(AnimeEntity anime, PollEntity poll) {}
