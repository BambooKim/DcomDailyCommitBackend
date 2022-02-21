package com.bamboo.exception;

public class NoUserExistsException extends Throwable {
    public NoUserExistsException(String msg) {
        super(msg);
    }
}
