package com.example.bankcards.repository;

import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByNumber(String number);

    @Modifying
    @Query("UPDATE Card c SET c.status = :status WHERE c.expiryDate < :today")
    int updateStatusForExpiredCards(@Param("today") LocalDate today, @Param("status") CardStatus status);
}
