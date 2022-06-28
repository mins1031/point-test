package com.example.pointapi.event.controller;

import com.example.pointapi.common.exception.ExceptionMessage;
import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.event.dto.EventOccurRequest;
import com.example.pointapi.event.service.EventService;
import com.example.pointapi.place.exception.NotFoundPlaceException;
import com.example.pointapi.review.reviewphoto.exception.NotFoundReviewPhotoException;
import com.example.pointapi.user.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotFoundUserException.class})
    public ResponseEntity<ExceptionMessage> handlerNotFoundUserException(NotFoundUserException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotFoundPlaceException.class})
    public ResponseEntity<ExceptionMessage> handlerNotFoundPlaceException(NotFoundPlaceException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotFoundReviewPhotoException.class})
    public ResponseEntity<ExceptionMessage> handlerNotFoundReviewPhotoException(NotFoundReviewPhotoException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
