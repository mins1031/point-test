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
        //포인트 증감을 하는 도메인은 분명히 리뷰뿐만이 아닐거라고 생각해 추상화 했습니다.
        eventTypeHandler.handleEvent(eventOccurRequest);
    }
}
