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
        ApiCallLog log = ApiCallLog.builder()
                .endpoint(endpoint)
                .method(method)
                .ip(ip)
                .country(country)
                .recordedAt(DateUtil.now())
                .build();
        apiCallLogRepository.save(log);
    }
}
