package com.hogimn.myanimechart.core.common.alarm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(name = "discord.webhookUrl")
public class DiscordAlarmService implements AlarmService {
    private final RestTemplate restTemplate;
    private final String webhookUrl;

    public DiscordAlarmService(RestTemplate restTemplate,
                               @Value("${discord.webhookUrl}")
                               String webhookUrl) {
        this.restTemplate = restTemplate;
        this.webhookUrl = webhookUrl;
    }

    @Async
    @Override
    public void send(String message) {
        try {
            Map<String, String> body = Map.of("content", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            restTemplate.postForLocation(webhookUrl, entity);
        } catch (Exception e) {
            log.error("Discord 알람 전송 중 오류 발생: {}", e.getMessage());
        }
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
