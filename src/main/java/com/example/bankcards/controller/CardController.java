package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.DataValidator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final DataValidator validator;

    @PostMapping
    public CardDto createCard(@RequestBody CardDto cardDto, HttpServletResponse response) {
        validator.objectIsNotNull(cardDto, "cardDto must not be null");
        response.setStatus(HttpServletResponse.SC_CREATED);
        return cardService.createCard(cardDto);
    }

    @GetMapping
    public List<CardDto> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public CardDto getCardById(@PathVariable Long id) {
        validator.objectIsNotNull(id, "id must not be null");
        return cardService.getCardById(id);
    }

}
