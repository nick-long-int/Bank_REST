package com.example.bankcards.service;

import com.example.bankcards.dto.RequestUserDto;
import com.example.bankcards.dto.ResponseUserDto;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.DataValidationException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtProducer jwtProducer;
    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Успешная регистрация")
    void testRegistrationSuccess() {
        RequestUserDto dto = new RequestUserDto();
        dto.setUsername("username");
        dto.setPassword("password");

        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));
        user.setId(1L);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtProducer.createToken(any())).thenReturn("token");

        ResponseUserDto responseUserDto = authService.register(dto);

        assertNotNull(responseUserDto);
        assertEquals(1L, responseUserDto.getId());
        assertEquals("username", responseUserDto.getUsername());
        assertNotNull(responseUserDto.getAccessToken());
    }

    @Test
    @DisplayName("Пользователь с таким username уже есть")
    void testRegistrationFailure() {
        RequestUserDto dto = new RequestUserDto();
        dto.setUsername("username");
        dto.setPassword("password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(DataValidationException.class, () -> authService.register(dto));
    }

}