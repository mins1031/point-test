package com.example.pointapi.event;

import com.example.pointapi.event.domain.Event;
import com.example.pointapi.event.domain.EventType;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Getter
@Component
public class EventHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<EventType, Event> pointApplyMapper = new HashMap<>();

    public EventHandler() {
        pointApplyMapper.put(EventType.REVIEW, );
    }

    public void handleEvent(EventType eventType, EventOccurRequest eventOccurRequest) {
        Event event = pointApplyMapper.get(eventType);
        event.handlePoint(eventOccurRequest);
    }
}
