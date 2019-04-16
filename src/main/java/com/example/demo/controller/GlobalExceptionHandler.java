package com.example.demo.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class GlobalExceptionHandler {

    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public String handleException(Throwable throwable) {



        return throwable.getMessage();
    }
}
