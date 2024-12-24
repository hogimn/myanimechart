package com.hogimn.myanimechart.database.repository;

import com.hogimn.myanimechart.database.dao.BatchDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<BatchDao, String> {
}
