package com.example.bankcards.exception;

import com.example.bankcards.exception.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalArgumentException(DataValidationException ex) {
        log.error(ex.getMessage(), ex);
        return ExceptionResponse.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
    }

    @ExceptionHandler(CardNumberValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleCardNumberValidationException(CardNumberValidationException ex) {
        log.error(ex.getMessage(), ex);
        return ExceptionResponse.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ExceptionResponse.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .build();
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleInsufficientFundsException(InsufficientFundsException ex) {
        log.error(ex.getMessage(), ex);
        return ExceptionResponse.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.FORBIDDEN.value())
            .build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ExceptionResponse.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .build();
    }

    @ExceptionHandler(MissingAuthHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleMissingAuthHeaderException(MissingAuthHeaderException ex) {
        log.error(ex.getMessage(), ex);
        return ExceptionResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.UNAUTHORIZED.value())
            .timestamp(LocalDateTime.now())
            .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ExceptionResponse.builder()
            .message(e.getMessage())
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();
    }
}
