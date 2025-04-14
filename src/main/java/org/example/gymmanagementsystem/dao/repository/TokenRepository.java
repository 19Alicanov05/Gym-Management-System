package org.example.gymmanagementsystem.dao.repository;

import org.example.gymmanagementsystem.dao.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<PasswordResetTokenEntity, Integer> {
    Optional<PasswordResetTokenEntity> findByEmailAndTokenAndIsUsedFalse(String email, String token);

    // Token'ın kullanılıp kullanılmadığını kontrol eden metod
    Optional<PasswordResetTokenEntity> findByToken(String token);

}
