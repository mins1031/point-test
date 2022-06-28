package com.example.pointapi.common.exceptionhandler;

import com.example.pointapi.common.exception.ExceptionMessage;
import com.example.pointapi.common.exception.ImpossibleException;
import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.user.exception.NotFoundUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionAdviceController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleException(Exception e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ImpossibleException.class})
    public ResponseEntity<ExceptionMessage> impossibleException(ImpossibleException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ExceptionMessage> handleNullPointerException(NullPointerException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotFoundUserException.class})
    public ResponseEntity<ExceptionMessage> handlerNotFoundUserException(NotFoundUserException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({WrongRequesterException.class})
    public ResponseEntity<ExceptionMessage> handlerWrongRequesterException(WrongRequesterException e) {
        log.error(e.getClass() + ": " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<ExceptionMessage>(ExceptionMessage.of(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
