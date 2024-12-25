package com.hogimn.myanimechart.database.batch.repository;

import com.hogimn.myanimechart.database.batch.dao.BatchDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<BatchDao, String> {
}
