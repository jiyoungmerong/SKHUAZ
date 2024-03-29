package com.app.skhuaz.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RspsTemplate<T> {
    private int statusCode;

    private String message;
    private T data;

    public RspsTemplate(HttpStatus httpStatus, T data) {
        this.statusCode = httpStatus.value();
        this.data = data;
    }

    public RspsTemplate(T data) {
        this.data = data;
    }

    public RspsTemplate(HttpStatus httpStatus, String message){
        this.statusCode = httpStatus.value();
        this.message = message;
    }

    public RspsTemplate(HttpStatus httpStatus, String message, T data) {
        this.statusCode = httpStatus.value();
        this.message = message;
        this.data = data;
    }
}
