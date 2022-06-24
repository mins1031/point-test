package com.example.pointapi.event.controller;

import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping(EventControllerPath.EVENT_OCCUR)
    public ResponseEntity<Void> occurEvent(
            @Validated @RequestBody EventOccurRequest eventOccurRequest
    ) {
        eventService.occurEvent(eventOccurRequest);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
