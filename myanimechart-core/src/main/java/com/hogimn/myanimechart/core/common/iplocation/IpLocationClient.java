package com.hogimn.myanimechart.core.common.iplocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class IpLocationClient {
    private final RestTemplate restTemplate;
    private static final String API_URL = "http://ip-api.com/json/{ip}";

    public String getCountryByIp(String ip) {
        if (isLoopbackAddress(ip)) return "Local";

        try {
            IpResponse response = restTemplate.getForObject(API_URL, IpResponse.class, ip);

            if (response != null && "success".equals(response.status())) {
                return response.country();
            }

            log.warn("Failed to fetch location for IP: {}. Status: {}", ip,
                    response != null ? response.status() : "Empty Response");
        } catch (Exception e) {
            log.error("Exception occurred while fetching country for IP: {}", ip, e);
        }
        return "Unknown";
    }

    private boolean isLoopbackAddress(String ip) {
        return "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip);
    }
}