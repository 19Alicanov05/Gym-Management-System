package org.example.gymmanagementsystem.controller;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymmanagementsystem.model.CardDto;
import org.example.gymmanagementsystem.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor

public class CardController {

    private final CardService cardService;

    @GetMapping
    @Operation(
            summary = "List Cards"
    )
    public List<CardDto> getAllCards() {
        return cardService.getAllCards();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add Cards"
    )
    public void addCard(@RequestBody @Valid CardDto customerCardDto) {
        cardService.addCard(customerCardDto);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "List Card By Id"
    )
    public CardDto getCardById(@PathVariable Integer id) {
        return cardService.getCardById(id);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete Card"
    )
    public void deleteCardById(@PathVariable @Valid Integer id) {
        cardService.deleteCard(id);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update Card"
    )

    public void updateCard(@PathVariable Integer id,
                           @RequestBody CardDto customerCardDto) {
        cardService.updateCard(customerCardDto, id);
    }
}
