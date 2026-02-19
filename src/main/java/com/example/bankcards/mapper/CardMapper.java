package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UpdateCardDto;
import com.example.bankcards.entity.card.Card;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "user", ignore = true)
    Card cardDtoToCard(CardDto cardDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCard(UpdateCardDto cardDto, @MappingTarget Card card);

    @Mapping(source = "user.id", target = "userId")
    CardDto cardToCardDto(Card card);
}
