package org.example.gymmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.repository.CardRepository;
import org.example.gymmanagementsystem.exceptions.NotFoundException;
import org.example.gymmanagementsystem.exceptions.ValidationException;
import org.example.gymmanagementsystem.mapper.CardMapper;
import org.example.gymmanagementsystem.model.CardDto;
import org.example.gymmanagementsystem.service.CardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public List<CardDto> getAllCards() {
        log.info("getAllCards start");
        var cards = cardRepository.findAll();
        log.info("getAllCards end");
        return cardMapper.toDtoList(cards);
    }

    @Override
    public CardDto getCardById(Integer id) {
        log.info("getCardById start with id: {}", id);
        var cards = cardRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Card not found"));
        log.info("getCardById end with id: {}", id);
        return cardMapper.toDto(cards);
    }

    @Override
    public void addCard(CardDto cardDto) {
        if (cardDto.getCardNumber().length() < 8) {
            log.error("Card number is too short");
            throw new ValidationException("BAD_REQUEST");
        }

        log.info("Start addCard");
        var cardEntity = cardMapper.toEntity(cardDto);

        log.debug("Mapped CardEntity: {}", cardEntity.getCardNumber());

        cardRepository.save(cardEntity);
        log.info("End addCard");
    }

    @Override
    public void deleteCard(Integer id) {
        log.info("deleteCard start with id: {}", id);
        cardRepository.deleteById(id);
        log.info("deleteCard end with id: {}", id);

    }

    @Override
    public void updateCard(CardDto cardDto, Integer id) {
        var cards = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CUSTOMER_NOT_FOUND"));
        log.info("Start updateCard with id: {}", id);

        if (cardDto.getCardNumber() != null) {
            cards.setCardNumber(cardDto.getCardNumber());
        }
        cardRepository.save(cards);
        log.info("End updateCustomer with id: {}", id);
    }


}
