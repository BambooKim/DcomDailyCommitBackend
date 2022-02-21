package com.bamboo.exception;

public class NoGithubIdFoundException extends Throwable {
    public NoGithubIdFoundException(String msg) {
        super(msg);
    }
}
