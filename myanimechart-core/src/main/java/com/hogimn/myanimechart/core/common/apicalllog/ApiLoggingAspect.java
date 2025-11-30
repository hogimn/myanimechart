package com.hogimn.myanimechart.core.common.apicalllog;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Aspect
@Component
@Slf4j
public class ApiLoggingAspect {
    private final ApiCallLogService apiCallLogService;
    private final RestTemplate restTemplate;

    public ApiLoggingAspect(
            ApiCallLogService apiCallLogService,
            RestTemplate restTemplate
    ) {
        this.apiCallLogService = apiCallLogService;
        this.restTemplate = restTemplate;
    }

    @Around("@within(com.hogimn.myanimechart.core.apicalllog.ApiLoggable)" +
            " || @annotation(com.hogimn.myanimechart.core.apicalllog.ApiLoggable)")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();

        String endpoint = request.getRequestURI();
        String method = request.getMethod();
        String ip = getClientIpAddr(request);
        String country = getCountryByIp(ip);

        apiCallLogService.saveLog(endpoint, method, ip, country);

        return joinPoint.proceed();
    }

    private String getClientIpAddr(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    private String getCountryByIp(String ip) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString("http://ip-api.com/json/")
                    .pathSegment(ip)
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);

            if (response != null && response.contains("\"status\":\"fail\"")) {
                log.warn("Failed to fetch country for IP: {}. Response: {}", ip, response);
                return "Unknown";
            }

            int countryIndex = response.indexOf("\"country\":\"");
            if (countryIndex != -1) {
                int startIndex = countryIndex + "\"country\":\"".length();
                int endIndex = response.indexOf("\"", startIndex);
                return response.substring(startIndex, endIndex);
            }

            log.warn("Country information not found in response for IP: {}", ip);
            return "Unknown";

        } catch (Exception e) {
            log.error("Error fetching country by IP");
            log.error(e.getMessage(), e);
            return "Unknown";
        }
    }
}
