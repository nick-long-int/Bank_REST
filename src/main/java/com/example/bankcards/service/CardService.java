package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.CardNumberValidationException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final DataValidator validator;

    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED
    )
    public CardDto createCard(CardDto cardDto) {

        validator.checkCardDtoFields(cardDto);

        if (cardRepository.findByNumber(cardDto.getNumber()).isPresent()) {
            throw new CardNumberValidationException("Card number is already exists");
        }
        Optional<User> user = userRepository.findById(cardDto.getUserId());
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Card card = cardMapper.cardDtoToCard(cardDto);
        card.setUser(user.get());
        return cardMapper.cardToCardDto(cardRepository.save(card));
    }

    @Transactional(readOnly = true)
    public List<CardDto> getAllCards() {
        return cardRepository.findAll()
            .stream()
            .map(cardMapper::cardToCardDto)
            .toList();
    }

    public CardDto getCardById(Long id) {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isEmpty()) {
            throw new NotFoundException("Card not found");
        }
        return cardMapper.cardToCardDto(card.get());
    }

    //todo написать тесты для метода, добавить sql script, разные окружения сделать, протестить в постмане
}
