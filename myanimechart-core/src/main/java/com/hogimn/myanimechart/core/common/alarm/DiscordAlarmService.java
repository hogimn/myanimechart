package com.hogimn.myanimechart.core.common.alarm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscordAlarmService implements AlarmService {
    private final RestTemplate restTemplate;
    private final String webhookUrl;

    public DiscordAlarmService(RestTemplate restTemplate,
                               @Value("${discord.webhookUrl}")
                               String webhookUrl) {
        this.restTemplate = restTemplate;
        this.webhookUrl = webhookUrl;
    }

    @Override
    public void send(String message) {
        String jsonPayload = String.format("{\"content\": \"%s\"}", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        restTemplate.postForObject(webhookUrl, entity, String.class);
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
