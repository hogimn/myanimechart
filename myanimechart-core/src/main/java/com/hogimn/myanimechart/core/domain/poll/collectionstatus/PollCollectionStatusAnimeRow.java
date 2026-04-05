package com.hogimn.myanimechart.core.domain.poll.collectionstatus;

import com.hogimn.myanimechart.core.domain.anime.AnimeEntity;

/**
 * JPQL constructor projection for {@code PollCollectionStatusEntity} joined with {@link AnimeEntity}.
 */
public record PollCollectionStatusAnimeRow(PollCollectionStatusEntity status, AnimeEntity anime) {}
