package com.hogimn.myanimechart.database.apicall.repository;

import com.hogimn.myanimechart.database.apicall.entity.ApiCallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, Long> {
}
