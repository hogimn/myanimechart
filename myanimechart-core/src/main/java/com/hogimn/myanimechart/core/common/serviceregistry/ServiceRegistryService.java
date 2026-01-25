package com.hogimn.myanimechart.core.common.serviceregistry;

import com.hogimn.myanimechart.core.common.exception.ServiceCommunicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor // 생성자 주입 간소화
public class ServiceRegistryService {
    private final RestTemplate restTemplate;

    public void send(RegisteredService service, String endpoint, Object body) {
        String url = String.format("http://%s/%s", service.name(), endpoint);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

            log.info("Request sent to {} successfully", url);
        } catch (RestClientException e) {
            log.error("Communication failed: {}", e.getMessage());
            throw new ServiceCommunicationException(service.name(), e);
        }
    }
}
