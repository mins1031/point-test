package com.example.pointapi.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExceptionMessage {

    private String errorMessage;

    public static ExceptionMessage of(String errorMessage) {
        return new ExceptionMessage(errorMessage);
    }
}
