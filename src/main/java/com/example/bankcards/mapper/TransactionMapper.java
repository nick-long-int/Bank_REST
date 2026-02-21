package com.example.bankcards.mapper;

import com.example.bankcards.dto.TransactionDto;
import com.example.bankcards.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "transaction.cardFrom.number", target = "cardNumberFrom")
    @Mapping(source = "transaction.cardTo.number", target = "cardNumberTo")
    TransactionDto toDto(Transaction transaction);

}
