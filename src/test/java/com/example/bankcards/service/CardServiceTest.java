package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UpdateCardDto;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.CardNumberValidationException;
import com.example.bankcards.exception.DataValidationException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardMapperImpl;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberMask;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private CardMapperImpl cardMapper;

    @Spy
    private DataValidator validator;

    @Spy
    private CardNumberMask mask;

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

    @Test
    @DisplayName("Поиск всех карт")
    void testGetAllCards(){
        List<Card> cards = List.of(
            new Card(),
            new Card(),
            new Card());

        cards.forEach(card -> card.setNumber("123456789876543"));

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
        card.setNumber("1234567898765432");

        User user = new User();
        user.setId(1L);

        card.setUser(user);

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        CardDto cardDto = cardService.getCardById(1L);
        assertNotNull(cardDto);
        assertEquals(1L, cardDto.getId());
        assertEquals("**** **** **** 5432", cardDto.getNumber());
        assertEquals(1L, cardDto.getUserId());
    }

    @Test
    @DisplayName("Удаление карты по ID")
    void testCardDeleteById(){
        Card card = new Card();

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        cardService.deleteCardById(1L);

        verify(cardRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).delete(card);
    }

    @Test
    @DisplayName("Обновление данных карты")
    void testUpdateCard(){
        UpdateCardDto updateCardDto = new UpdateCardDto();
        updateCardDto.setExpiryDate(LocalDate.MAX);
        updateCardDto.setStatus("BLOCKED");

        Card card = new Card();
        card.setId(1L);
        card.setExpiryDate(LocalDate.now());
        card.setNumber("1234567898765432");

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        CardDto cardDto = cardService.updateCard(1L, updateCardDto);

        assertEquals(1L, cardDto.getId());
        assertEquals("BLOCKED", cardDto.getStatus());
        assertEquals(LocalDate.MAX, cardDto.getExpiryDate());
        assertEquals("**** **** **** 5432", cardDto.getNumber());

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

    @Test
    @DisplayName("Карта не найдена")
    void testGetCardByIdWhenCardNotExist(){
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardService.getCardById(1L));
    }

    @Test
    @DisplayName("Удаление карты с несуществующим ID")
    void testDeleteCardWithIdWhichNotExist(){
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardService.deleteCardById(1L));
    }


    @Test
    @DisplayName("Обновление карты с несуществующим ID")
    void testUpdateCardWithIdWhichNotExist(){
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> cardService.updateCard(1L, new UpdateCardDto()));
    }

    @Test
    @DisplayName("Обновление карты с сроком годности до текущей даты")
    void testUpdateCardWithExpiryDateBeforeNow(){
        UpdateCardDto updateCardDto = new UpdateCardDto();
        updateCardDto.setExpiryDate(LocalDate.now().minusDays(1));

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(new Card()));

        assertThrows(DataValidationException.class,
            () -> cardService.updateCard(1L, updateCardDto));
    }

}