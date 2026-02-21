package com.example.bankcards.util;

import com.example.bankcards.entity.user.User;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProducer {
    private final PrivateKey privateKey;

    public String createToken(User userCredential) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userCredential.getRole());
        claims.put("userId", userCredential.getId());

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userCredential.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
            .signWith(privateKey)
            .compact();
    }
}
