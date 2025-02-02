package com.hogimn.myanimechart.database.apicalllog.service;

import com.hogimn.myanimechart.common.util.DateUtil;
import com.hogimn.myanimechart.database.apicalllog.entity.ApiCallLog;
import com.hogimn.myanimechart.database.apicalllog.repository.ApiCallLogRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiCallLogService {
    private final ApiCallLogRepository apiCallLogRepository;

    public ApiCallLogService(ApiCallLogRepository apiCallLogRepository) {
        this.apiCallLogRepository = apiCallLogRepository;
    }

    public void saveLog(String endpoint, String method, String ip, String country) {
        ApiCallLog log = new ApiCallLog();
        log.setEndpoint(endpoint);
        log.setMethod(method);
        log.setIp(ip);
        log.setCountry(country);
        log.setRecordedAt(DateUtil.now());

        apiCallLogRepository.save(log);
    }
}
