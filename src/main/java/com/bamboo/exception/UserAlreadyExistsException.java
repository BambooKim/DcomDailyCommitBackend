package com.bamboo.exception;

public class UserAlreadyExistsException extends Throwable {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
