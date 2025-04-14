package org.example.gymmanagementsystem.dao.repository;

import org.example.gymmanagementsystem.dao.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity,Integer> {
}
