package com.example.pointapi.user.controller;

import com.example.pointapi.common.exception.ExceptionMessage;
import com.example.pointapi.event.review.exception.NotFoundReviewException;
import com.example.pointapi.user.dto.PointFindResponse;
import com.example.pointapi.user.exception.NotFoundUserException;
import com.example.pointapi.user.exception.NotMatchPresentPoint;
import com.example.pointapi.user.service.PointFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointFindController {
    private final PointFindService pointFindService;

    @GetMapping(PointFindControllerPath.POINT_FIND)
    public ResponseEntity<PointFindResponse> findUserPoint(@PathVariable String userId) {
        PointFindResponse pointFindResponse = pointFindService.findUserPoint(userId);
        return new ResponseEntity<PointFindResponse>(pointFindResponse, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NotFoundReviewException.class})
    public ResponseEntity<ExceptionMessage> handlerNotFoundReviewException(NotFoundReviewException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
