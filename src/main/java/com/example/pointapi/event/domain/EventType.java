package com.example.pointapi.event.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EventType {
    REVIEW("REVIEW");

    private String desc;

    EventType(String desc) {
        this.desc = desc;
    }

    public static EventType catchEventType(String eventDesc) {
        return Arrays.stream(EventType.values())
                .filter(eventType -> eventType.getDesc().equals(eventDesc))
                .findAny()
                .orElse(null);
    }
}
