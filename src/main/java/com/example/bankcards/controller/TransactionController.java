package com.example.bankcards.controller;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.service.TransactionService;
import com.example.bankcards.util.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final DataValidator validator;

    @PostMapping
    public TransactionDto runTransaction(@RequestBody TransactionDto transactionDto) {
        validator.objectIsNotNull(transactionDto, "dto must not be null");
        return transactionService.runTransaction(transactionDto);
    }
}
