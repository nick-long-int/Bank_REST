package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.mapper.TransactionMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardRepository cardRepository;
    @Spy
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Успешный перевод")
    void testTransactionSuccess() {
        TransactionDto dto = new TransactionDto();
        dto.setCardNumberTo("123456789");
        dto.setCardNumberFrom("987654321");
        dto.setAmount(BigDecimal.valueOf(1000));

        Card cardFrom = new Card();
        cardFrom.setBalance(BigDecimal.valueOf(1500));
        Card cardTo = new Card();
        cardTo.setBalance(BigDecimal.valueOf(100));

        when(cardRepository.findByNumber("987654321")).thenReturn(Optional.of(cardFrom));
        when(cardRepository.findByNumber("123456789")).thenReturn(Optional.of(cardTo));

        transactionService.runTransaction(dto);

        assertEquals(BigDecimal.valueOf(500), cardFrom.getBalance());
        assertEquals(BigDecimal.valueOf(1100), cardTo.getBalance());
    }

    @Test
    @DisplayName("Карта на которую переводят не найдена")
    void testTransactionFailureWhenCardToNotFound() {
        TransactionDto dto = new TransactionDto();
        dto.setCardNumberTo("123456789");
        dto.setCardNumberFrom("987654321");
        dto.setAmount(BigDecimal.valueOf(1000));

        when(cardRepository.findByNumber("987654321")).thenReturn(Optional.of(new Card()));
        when(cardRepository.findByNumber("123456789")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transactionService.runTransaction(dto));
    }

    @Test
    @DisplayName("Карта с которой переводят не найдена")
    void testTransactionFailureWhenCardFromNotFound() {
        TransactionDto dto = new TransactionDto();
        dto.setCardNumberTo("123456789");
        dto.setCardNumberFrom("987654321");
        dto.setAmount(BigDecimal.valueOf(1000));

        when(cardRepository.findByNumber("987654321")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transactionService.runTransaction(dto));
    }

    @Test
    @DisplayName("Перевод при нехватке средств")
    void testTransactionFailureWhenInsufficientBalance() {
        TransactionDto dto = new TransactionDto();
        dto.setCardNumberTo("123456789");
        dto.setCardNumberFrom("987654321");
        dto.setAmount(BigDecimal.valueOf(1000));

        Card cardFrom = new Card();
        cardFrom.setBalance(BigDecimal.valueOf(500));
        Card cardTo = new Card();
        cardTo.setBalance(BigDecimal.valueOf(100));

        when(cardRepository.findByNumber("987654321")).thenReturn(Optional.of(cardFrom));
        when(cardRepository.findByNumber("123456789")).thenReturn(Optional.of(cardTo));

        assertThrows(InsufficientFundsException.class, () -> transactionService.runTransaction(dto));
    }

}