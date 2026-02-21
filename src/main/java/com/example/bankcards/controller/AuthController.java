package com.example.bankcards.controller;

import com.example.bankcards.dto.RequestUserDto;
import com.example.bankcards.dto.ResponseUserDto;
import com.example.bankcards.service.AuthService;
import com.example.bankcards.util.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final DataValidator validator;

    @PostMapping("/register")
    public ResponseUserDto register(@RequestBody RequestUserDto requestUserDto) {
        validator.objectIsNotNull(requestUserDto, "requestUserDto must not be null");
        return authService.register(requestUserDto);
    }
}
