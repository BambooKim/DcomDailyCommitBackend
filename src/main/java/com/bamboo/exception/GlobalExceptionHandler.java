package com.bamboo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // register001 - 입력한 github id 없을 때
    @ExceptionHandler(NoGithubIdFoundException.class)
    protected ResponseEntity<?> handleNoGithubID(NoGithubIdFoundException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("REG-001")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    // register002 - 입력한 access code가 잘못되었을 때
    @ExceptionHandler(WrongAccessCodeException.class)
    protected ResponseEntity<?> handleWrongAccessCode(WrongAccessCodeException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("REG-002")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    // register003 - 입력한 유저가 이미 존재할 때
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<?> handleDuplicateUser(UserAlreadyExistsException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("REG-003")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    // delete001 - 입력한 유저 정보가 없을 때
    @ExceptionHandler(NoUserExistsException.class)
    protected ResponseEntity<?> handleWrongAccessCode(NoUserExistsException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("DEL-001")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    // delete002 - 입력한 유저 코드가 잘못되었을 때
    @ExceptionHandler(WrongUserCodeException.class)
    protected ResponseEntity<?> handleWrongUserCode(WrongUserCodeException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("DEL-002")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    // common001 - body의 field가 비어있을 때
    @ExceptionHandler(BlankArgumentException.class)
    protected ResponseEntity<?> handleBlankArg(BlankArgumentException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("COM-001")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    // common002 - argument의 타입이 올바르지 않을 때
    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<?> handleArgTypeUnmatch(NumberFormatException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("COM-002")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }
}
