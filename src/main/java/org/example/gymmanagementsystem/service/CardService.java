package org.example.gymmanagementsystem.service;

import org.example.gymmanagementsystem.model.CardDto;

import java.util.List;

public interface CardService {

    List<CardDto> getAllCards();
    CardDto getCardById(Integer id);
    void addCard(CardDto cardDto);
    void deleteCard(Integer id);
    void updateCard(CardDto cardDto, Integer id);
}
