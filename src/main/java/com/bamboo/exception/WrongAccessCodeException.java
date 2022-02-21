package com.bamboo.exception;

public class WrongAccessCodeException extends Throwable {
    public WrongAccessCodeException(String msg) {
        super(msg);
    }
}
