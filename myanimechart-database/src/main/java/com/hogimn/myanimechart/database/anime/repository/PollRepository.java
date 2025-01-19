package com.hogimn.myanimechart.database.anime.repository;

import com.hogimn.myanimechart.database.anime.dao.AnimeDao;
import com.hogimn.myanimechart.database.anime.dao.PollDao;
import com.hogimn.myanimechart.database.anime.dao.PollOptionDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollRepository extends JpaRepository<PollDao, Long> {
    Optional<PollDao> findByAnimeAndPollOptionAndTopicId(AnimeDao animeDao, PollOptionDao pollOption, Long topicId);
}