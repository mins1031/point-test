package com.example.pointapi.event;

import com.example.pointapi.event.review.ReviewEvent;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.exception.WrongEventTypeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Getter
@Component
public class EventTypeHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<EventType, Event> pointApplyMapper = new HashMap<>();

    public EventTypeHandler(
            ReviewEvent reviewEvent
    ) {
        pointApplyMapper.put(EventType.REVIEW, reviewEvent);
    }

    public void handleEvent(EventOccurRequest eventOccurRequest) {
        EventType eventType = EventType.catchEventType(eventOccurRequest.getEventType());
        Event event = pointApplyMapper.get(eventType);
        event.handlePoint(eventOccurRequest);
    }
}
