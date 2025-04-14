package org.example.gymmanagementsystem.dao.repository;

import org.example.gymmanagementsystem.dao.entity.GymEntryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymEntryLogRepository extends JpaRepository<GymEntryLogEntity, Long> {
    List<GymEntryLogEntity> findByCustomerId(Integer customerId);
}
