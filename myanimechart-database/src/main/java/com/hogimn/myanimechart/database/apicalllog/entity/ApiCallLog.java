package com.hogimn.myanimechart.database.apicalllog.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_call_log")
@Data
public class ApiCallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpoint;
    private String method;
    private String ip;
    private String country;
    private LocalDateTime recordedAt;
}
