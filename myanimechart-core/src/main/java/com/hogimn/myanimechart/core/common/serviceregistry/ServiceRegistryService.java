package com.hogimn.myanimechart.core.common.serviceregistry;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ServiceRegistryService {
    private final EurekaClient eurekaClient;
    private final RestTemplate restTemplate;

    public ServiceRegistryService(EurekaClient eurekaClient, RestTemplate restTemplate) {
        this.eurekaClient = eurekaClient;
        this.restTemplate = restTemplate;
    }

    public String getServiceUrl(RegisteredService service) {
        String serviceName = service.toString();
        try {
            InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(serviceName, false);

            if (instanceInfo != null) {
                String serviceUrl = instanceInfo.getHomePageUrl();
                log.info("Service URL for {}: {}", serviceName, serviceUrl);
                return serviceUrl;
            } else {
                throw new IllegalArgumentException("Service not found: " + serviceName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }
    }

    public void send(RegisteredService service, String endpoint, String body) {
        try {
            String serviceUrl = getServiceUrl(service);
            if (serviceUrl != null) {
                String fullUrl = serviceUrl + "/" + endpoint;

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);

                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                restTemplate.exchange(fullUrl, HttpMethod.POST, entity, Void.class);
                log.info("Data sent to {}: {}", service, body);
            }
        } catch (Exception e) {
            log.error("Error sending data to service {}: {}", service.toString(), e.getMessage(), e);
        }
    }

    public void send(RegisteredService service, String endpoint, Object body) {
        try {
            String serviceUrl = getServiceUrl(service);
            if (serviceUrl != null) {
                String fullUrl = serviceUrl + "/" + endpoint;

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Object> entity = new HttpEntity<>(body, headers);

                restTemplate.exchange(fullUrl, HttpMethod.POST, entity, Void.class);
                log.info("Data sent to {}: {}", service, body);
            }
        } catch (Exception e) {
            log.error("Error sending data to service {}: {}", service.toString(), e.getMessage(), e);
        }
    }
}
