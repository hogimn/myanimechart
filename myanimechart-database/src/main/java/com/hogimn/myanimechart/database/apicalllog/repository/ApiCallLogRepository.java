package com.hogimn.myanimechart.database.apicalllog.repository;

import com.hogimn.myanimechart.database.apicalllog.entity.ApiCallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, Long> {
}
