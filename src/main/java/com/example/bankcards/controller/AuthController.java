package com.example.bankcards.controller;

import com.example.bankcards.dto.RequestUserDto;
import com.example.bankcards.dto.ResponseUserDto;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.service.AuthService;
import com.example.bankcards.util.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseUserDto register(@RequestBody RequestUserDto requestUserDto) {
        validator.objectIsNotNull(requestUserDto, "requestUserDto must not be null");
        return authService.register(requestUserDto);
    }

    @PostMapping("/login")
    public ResponseUserDto login(@RequestBody RequestUserDto requestUserDto) {
        validator.objectIsNotNull(requestUserDto, "requestUserDto must not be null");
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(requestUserDto.getUsername(), requestUserDto.getPassword())
        );
        if (auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();
            return authService.login(user);
        }
        throw new BadCredentialsException("Invalid username or password");
    }
}
