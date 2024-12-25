package com.hogimn.myanimechart.common.alarm;

public interface AlarmService {
    void send(String message);
    boolean isSupported();
}
