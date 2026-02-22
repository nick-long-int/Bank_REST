package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.TransactionMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRES_NEW
    )
    public TransactionDto runTransaction(TransactionDto transactionDto) {
        Card cardFrom = cardRepository.findByNumber(transactionDto.getCardNumberFrom())
            .orElseThrow(()-> new NotFoundException("Card not found"));
        Card cardTo = cardRepository.findByNumber(transactionDto.getCardNumberTo())
            .orElseThrow(() -> new NotFoundException("Card not found"));

        if (cardFrom.getBalance().compareTo(transactionDto.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        cardFrom.setBalance(cardFrom.getBalance().subtract(transactionDto.getAmount()));
        cardTo.setBalance(cardTo.getBalance().add(transactionDto.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setCardFrom(cardFrom);
        transaction.setCardTo(cardTo);
        transaction.setDate(LocalDateTime.now());

        return transactionMapper.toDto(transactionRepository.save(transaction));
    }
}
