package com.hogimn.myanimechart.database.batch.repository;

import com.hogimn.myanimechart.database.batch.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<BatchEntity, String> {
}
