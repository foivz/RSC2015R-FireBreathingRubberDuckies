package com.example.loginmodule.model.exception;

/**
 * Created by david on 15.11.2015..
 */
public class GetUserException extends RuntimeException {

    private int code;

    public int getCode() {
        return code;
    }

    public GetUserException(int code) {
        this.code = code;
    }
}
