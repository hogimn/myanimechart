package com.hogimn.myanimechart.database.anime.repository;

import com.hogimn.myanimechart.database.anime.dao.PollOptionDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollOptionRepository extends JpaRepository<PollOptionDao, Integer> {
    Optional<PollOptionDao> findByText(String text);
}