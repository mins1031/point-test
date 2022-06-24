package com.example.pointapi.event.domain;

import com.example.pointapi.event.dto.EventOccurRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewEvent implements Event {


    @Override
    public void handlePoint(EventOccurRequest eventOccurRequest) {


    }
}
