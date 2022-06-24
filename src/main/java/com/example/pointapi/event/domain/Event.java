package com.example.pointapi.event.domain;

import com.example.pointapi.event.dto.EventOccurRequest;

public interface Event {
    void handlePoint(EventOccurRequest eventOccurRequest);
}
