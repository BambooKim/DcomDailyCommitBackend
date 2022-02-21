package com.bamboo.exception;

public class WrongUserCodeException extends Throwable {
    public WrongUserCodeException(String msg) {
        super(msg);
    }
}
