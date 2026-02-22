package com.example.bankcards.service;

import com.example.bankcards.dto.RequestUserDto;
import com.example.bankcards.dto.ResponseUserDto;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.entity.user.UserRole;
import com.example.bankcards.exception.DataValidationException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProducer jwtProducer;

    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED
    )
    public ResponseUserDto register(RequestUserDto requestUserDto) {
        if (userRepository.findByUsername(requestUserDto.getUsername()).isPresent()) {
            throw new DataValidationException("Username is already in use");
        }

        User user = new User();
        user.setUsername(requestUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));
        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);
        return new ResponseUserDto(savedUser.getId(), savedUser.getUsername(), jwtProducer.createToken(savedUser));

    }

    public ResponseUserDto login(User user) {
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setUsername(user.getUsername());
        responseUserDto.setAccessToken(jwtProducer.createToken(user));
        return responseUserDto;
    }
}
