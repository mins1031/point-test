package com.example.pointapi.event;

import com.example.pointapi.event.exception.WrongEventTypeException;
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
                .orElseThrow(WrongEventTypeException::new);
    }
}
