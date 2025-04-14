package org.example.gymmanagementsystem.dao.repository;

import org.example.gymmanagementsystem.dao.entity.BlacklistedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlackListedTokenRepository extends JpaRepository<BlacklistedTokenEntity, Integer> {
    boolean existsByToken(String token);
}
