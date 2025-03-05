package com.hogimn.myanimechart.database.poll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollOptionRepository extends JpaRepository<PollOptionEntity, Integer> {
    Optional<PollOptionEntity> findByText(String text);
}