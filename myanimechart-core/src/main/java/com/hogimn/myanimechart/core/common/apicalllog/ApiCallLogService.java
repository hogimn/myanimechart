package com.hogimn.myanimechart.core.common.apicalllog;

import com.hogimn.myanimechart.core.common.iplocation.IpLocationClient;
import com.hogimn.myanimechart.core.common.util.DateUtil;
import com.hogimn.myanimechart.core.common.util.IpMaskingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiCallLogService {
    private final ApiCallLogRepository apiCallLogRepository;
    private final IpLocationClient ipLocationClient;

    @Async
    public void recordApiCall(String endpoint, String method, String ip) {
        try {
            String country = ipLocationClient.getCountryByIp(ip);

            String maskedIp = IpMaskingUtil.mask(ip);

            ApiCallLog logEntity = ApiCallLog.builder()
                    .endpoint(endpoint)
                    .method(method)
                    .ip(maskedIp)
                    .country(country)
                    .recordedAt(LocalDateTime.now())
                    .build();

            apiCallLogRepository.save(logEntity);
        } catch (Exception e) {
            log.error("Failed to process API call log for IP: {}", ip, e);
        }
    }
}
