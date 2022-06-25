package com.example.pointapi.event.service;

import com.example.pointapi.event.EventTypeHandler;
import com.example.pointapi.event.dto.EventOccurRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventTypeHandler eventTypeHandler;

    @Transactional
    public void occurEvent(EventOccurRequest eventOccurRequest) {
        eventTypeHandler.handleEvent(eventOccurRequest);
    }
}
