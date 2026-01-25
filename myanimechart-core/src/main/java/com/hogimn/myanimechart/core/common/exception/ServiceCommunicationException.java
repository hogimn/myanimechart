package com.hogimn.myanimechart.core.common.exception;

public class ServiceCommunicationException extends RuntimeException {
    public ServiceCommunicationException(String serviceName, Throwable cause) {
        super(String.format("Error occurred while communicating with service: %s", serviceName), cause);
    }
}