package org.example.gymmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.entity.TrainerEntity;
import org.example.gymmanagementsystem.dao.repository.TrainerRepository;
import org.example.gymmanagementsystem.exceptions.NotFoundException;
import org.example.gymmanagementsystem.mapper.TrainerMapper;
import org.example.gymmanagementsystem.model.TrainerDto;
import org.example.gymmanagementsystem.service.TrainerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    @Override
    public List<TrainerDto> getAllTrainers() {
        log.info("Fetching all trainers...");

        var trainers = trainerRepository.findAll();

        log.info("Found {} trainers.", trainers.size());
        return trainerMapper.toDtoList(trainers);
    }

    @Override
    public TrainerDto getTrainerById(Integer id) {
        log.info("Fetching trainer with ID: {}", id);

        var trainer = trainerRepository.findById(id).orElseThrow(() -> {
            log.warn("Trainer with ID {} not found.", id);
            return new NotFoundException("Trainer not found");
        });

        log.info("Successfully fetched trainer: {} {}", trainer.getName(), trainer.getSurname());
        return trainerMapper.toDto(trainer);
    }

    @Override
    public void deleteTrainer(Integer id) {
        log.info("Attempting to delete trainer with ID: {}", id);

        if (!trainerRepository.existsById(id)) {
            log.warn("Trainer with ID {} does not exist. Skipping deletion.", id);
            throw new NotFoundException("Trainer not found");
        }

        trainerRepository.deleteById(id);
        log.info("Successfully deleted trainer with ID: {}", id);
    }

    @Override
    public void addTrainer(TrainerDto trainerDto) {
        log.info("Adding new trainer: {} {}", trainerDto.getName(), trainerDto.getSurname());

        var trainer = trainerMapper.toEntity(trainerDto);
        trainerRepository.save(trainer);

        log.info("Trainer added successfully with ID: {}", trainer.getId());
    }

    @Override
    public void updateTrainer(Integer id, TrainerDto trainerDto) {
        log.info("Updating trainer with ID: {}", id);

        TrainerEntity existingTrainer = trainerRepository.findById(id).orElseThrow(() -> {
            log.warn("Trainer with ID {} not found, update failed.", id);
            return new NotFoundException("TRAINER_NOT_FOUND");
        });

        if (trainerDto.getSurname() != null) {
            log.info("Updating surname for trainer ID {}: {} -> {}", id, existingTrainer.getSurname(), trainerDto.getSurname());
            existingTrainer.setSurname(trainerDto.getSurname());
        }

        if (trainerDto.getBirthDate() != null) {
            log.info("Updating birthdate for trainer ID {}: {} -> {}", id, existingTrainer.getBirthDate(), trainerDto.getBirthDate());
            existingTrainer.setBirthDate(trainerDto.getBirthDate());
        }

        trainerRepository.save(existingTrainer);
        log.info("Successfully updated trainer with ID: {}", id);
    }

    @Override
    public List<String> getCustomersByTrainer(Integer trainerId) {
        Optional<TrainerEntity> trainerOptional = trainerRepository.findById(trainerId);
        if (trainerOptional.isEmpty()) {
            throw new NotFoundException("Trainer not found.");
        }

        TrainerEntity trainer = trainerOptional.get();


        return trainer.getCustomers().stream()
                .map(customer -> customer.getName() + " " + customer.getSurname())
                .collect(Collectors.toList());
    }
}
