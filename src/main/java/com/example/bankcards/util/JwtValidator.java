package com.example.bankcards.util;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private final PublicKey publicKey;

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
