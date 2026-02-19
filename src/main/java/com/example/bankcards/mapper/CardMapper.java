package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.card.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "user", ignore = true)
    Card cardDtoToCard(CardDto cardDto);

    @Mapping(source = "user.id", target = "userId")
    CardDto cardToCardDto(Card card);
}
