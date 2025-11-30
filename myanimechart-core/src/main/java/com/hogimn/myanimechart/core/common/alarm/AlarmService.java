package com.hogimn.myanimechart.core.common.alarm;

public interface AlarmService {
    void send(String message);
    boolean isSupported();
}
