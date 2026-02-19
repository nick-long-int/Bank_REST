package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardMapperImpl;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.DataValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceGetTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private CardMapperImpl cardMapper;

    @Spy
    private DataValidator validator;

    @InjectMocks
    private CardService cardService;

    @Test
    @DisplayName("Поиск всех карт")
    void testGetAllCards(){
        List<Card> cards = List.of(
            new Card(),
            new Card(),
            new Card());

        when(cardRepository.findAll()).thenReturn(cards);

        List<CardDto> cardDtos = cardService.getAllCards();
        assertNotNull(cardDtos);
        assertEquals(cardDtos.size(), cards.size());
    }

    @Test
    @DisplayName("Успешный поиск карты по ID")
    void testGetCardById(){
        Card card = new Card();
        card.setId(1L);
        card.setNumber("1234_5678_9876_5432");

        User user = new User();
        user.setId(1L);

        card.setUser(user);

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        CardDto cardDto = cardService.getCardById(1L);
        assertNotNull(cardDto);
        assertEquals(1L, cardDto.getId());
        assertEquals("1234_5678_9876_5432", cardDto.getNumber());
        assertEquals(1L, cardDto.getUserId());
    }

    @Test
    @DisplayName("Карта не найдена")
    void testGetCardByIdWhenCardNotExist(){
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardService.getCardById(1L));
    }
}
