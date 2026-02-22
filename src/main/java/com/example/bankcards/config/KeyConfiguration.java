package com.example.bankcards.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class KeyConfiguration {

    @Value("${key.public}")
    private String publicKeyPath;
    @Value("${key.private}")
    private String privateKeyPath;

    @Bean
    public PublicKey publicKey() {
        String keyContent = readKeyFromFile(publicKeyPath);
        keyContent = keyContent.replace("-----BEGIN PUBLIC KEY-----", "");
        keyContent = keyContent.replace("-----END PUBLIC KEY-----", "");
        keyContent = keyContent.replaceAll("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(keyContent);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        try {
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PrivateKey privateKey() {
        String keyContent = readKeyFromFile(privateKeyPath);
        keyContent = keyContent.replace("-----BEGIN PRIVATE KEY-----", "");
        keyContent = keyContent.replace("-----END PRIVATE KEY-----", "");
        keyContent = keyContent.replaceAll("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String readKeyFromFile(String pathFile) {
        ClassPathResource resource = new ClassPathResource(pathFile);
        try (InputStream inputStream = resource.getInputStream()) {

            return new String(inputStream.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
