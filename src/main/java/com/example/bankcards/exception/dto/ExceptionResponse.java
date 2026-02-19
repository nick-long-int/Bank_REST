package com.example.bankcards.exception.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ExceptionResponse {
    private String message;
    private LocalDateTime timestamp;
    private int status;
}
