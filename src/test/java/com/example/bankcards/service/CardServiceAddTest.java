package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.CardNumberValidationException;
import com.example.bankcards.exception.DataValidationException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardMapperImpl;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.DataValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceAddTest {

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

    private CardDto dto;

    @BeforeEach
    void setUp() {
        dto = new CardDto();
        dto.setNumber("1234_5678_9876_5432");
        dto.setExpiryDate(LocalDate.MAX);
        dto.setStatus("ACTIVE");
        dto.setUserId(1L);
    }

    //Positive tests

    @Test
    @DisplayName("Успешное создание карты")
    void testCreateCardSuccess() {

        User user = new User();
        user.setId(1L);

        Card card = cardMapper.cardDtoToCard(dto);
        card.setId(1L);
        card.setUser(user);

        when(cardRepository.findByNumber(anyString())).thenReturn(Optional.<Card>empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(cardRepository.save(any(Card.class))).thenReturn(card);


        CardDto createdCard = cardService.createCard(dto);
        assertEquals(1L, createdCard.getId());
        assertEquals(1L, createdCard.getUserId());
        verify(cardRepository, Mockito.times(1)).findByNumber(anyString());
        verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    //Negative tests

    @Test
    @DisplayName("Тест для создания карты с существующим номером")
    void testCreateCardWhenNumberExists() {
        when(cardRepository.findByNumber(anyString())).thenReturn(Optional.of(new Card()));

        assertThrows(CardNumberValidationException.class,
            () -> cardService.createCard(dto));
        verify(cardRepository, Mockito.times(1)).findByNumber(anyString());
        verify(userRepository, Mockito.times(0)).findById(anyLong());
        verify(cardRepository, Mockito.times(0)).save(any(Card.class));
    }

    @Test
    @DisplayName("Создание карты для несуществующего пользователя")
    void testCreateCardWithUserWhichNotExists() {
        when(cardRepository.findByNumber(anyString())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> cardService.createCard(dto));
        verify(cardRepository, Mockito.times(1)).findByNumber(anyString());
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        verify(cardRepository, Mockito.times(0)).save(any(Card.class));
    }

    @Test
    @DisplayName("Создание карты с пустым номером")
    void testCreateCardWithEmptyNumber() {
        dto.setNumber("");

        assertThrows(DataValidationException.class,
            () -> cardService.createCard(dto));
    }

    @Test
    @DisplayName("Создание карты с пустым сроком годности")
    void testCreateCardWithEmptyExpiryDate() {
        dto.setExpiryDate(null);

        assertThrows(DataValidationException.class,
            () -> cardService.createCard(dto));
    }

    @Test
    @DisplayName("Создание карты с сроком годности до даты создания")
    void testCreateCardWithExpiryDateBeforeCreatedDate() {
        dto.setExpiryDate(LocalDate.now().minusDays(1));

        assertThrows(DataValidationException.class,
            () -> cardService.createCard(dto));
    }

    @Test
    @DisplayName("Создание карты с пустым статусом")
    void testCreateCardWithBlankStatus() {
        dto.setStatus("");

        assertThrows(DataValidationException.class,
            () -> cardService.createCard(dto));
    }

    @Test
    @DisplayName("Создание карты без пользователя")
    void testCreateCardWithoutUserId() {
        dto.setUserId(null);

        assertThrows(DataValidationException.class,
            () -> cardService.createCard(dto));
    }


}