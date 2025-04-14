package org.example.gymmanagementsystem.controller;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.model.TrainerDto;
import org.example.gymmanagementsystem.service.impl.TrainerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Slf4j
public class TrainerController {
    private final TrainerServiceImpl trainerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all trainers"
    )
    public List<TrainerDto> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete trainer"
    )
    public void deleteTrainer(@PathVariable Integer id) {
        trainerService.deleteTrainer(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get trainer by id"
    )
    public TrainerDto getTrainerById(@PathVariable Integer id) {
        return trainerService.getTrainerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add Trainer"
    )

    public void addTrainer(@RequestBody @Valid TrainerDto trainerDto) {
        trainerService.addTrainer(trainerDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update trainer"
    )
    public void updateTrainer(@PathVariable Integer id, @RequestBody @Valid TrainerDto trainerDto) {
        trainerService.updateTrainer(id, trainerDto);

    }

    @GetMapping("/{trainerId}/customers")
    @Operation(
            summary = "Get customers by trainer"
    )
    public ResponseEntity<List<String>> getCustomersByTrainer(@PathVariable Integer trainerId) {
        List<String> customerNames = trainerService.getCustomersByTrainer(trainerId);
        return ResponseEntity.ok(customerNames);
    }

}
