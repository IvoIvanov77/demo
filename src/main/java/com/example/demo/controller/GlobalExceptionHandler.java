package com.example.demo.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class GlobalExceptionHandler {

    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public String handleException(Throwable e) {

        Throwable throwable = e;

        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }

        return throwable.getMessage();
    }
}
