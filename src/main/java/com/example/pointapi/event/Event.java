package com.example.pointapi.event;

import com.example.pointapi.event.dto.EventOccurRequest;

public interface Event {
    void handlePoint(EventOccurRequest eventOccurRequest);
}
