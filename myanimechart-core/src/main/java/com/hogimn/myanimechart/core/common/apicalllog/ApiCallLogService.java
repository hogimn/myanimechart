package com.hogimn.myanimechart.core.common.apicalllog;

import com.hogimn.myanimechart.core.common.util.DateUtil;
import org.springframework.stereotype.Service;

@Service
public class ApiCallLogService {
    private final ApiCallLogRepository apiCallLogRepository;

    public ApiCallLogService(ApiCallLogRepository apiCallLogRepository) {
        this.apiCallLogRepository = apiCallLogRepository;
    }

    public void save(String endpoint, String method, String ip, String country) {
        ApiCallLog log = new ApiCallLog();
        log.setEndpoint(endpoint);
        log.setMethod(method);
        log.setIp(ip);
        log.setCountry(country);
        log.setRecordedAt(DateUtil.now());

        apiCallLogRepository.save(log);
    }
}
