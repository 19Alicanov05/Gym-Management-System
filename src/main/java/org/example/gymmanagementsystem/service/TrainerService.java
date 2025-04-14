package org.example.gymmanagementsystem.service;

import org.example.gymmanagementsystem.model.TrainerDto;

import java.util.List;

public interface TrainerService {
    List<TrainerDto> getAllTrainers();
    void addTrainer(TrainerDto trainerDto);
    void deleteTrainer(Integer id);
    TrainerDto getTrainerById(Integer id);
    void updateTrainer(Integer id, TrainerDto trainerDto);
    List<String> getCustomersByTrainer(Integer trainerId);
}
