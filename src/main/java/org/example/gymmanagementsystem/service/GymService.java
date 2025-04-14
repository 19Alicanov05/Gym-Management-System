package org.example.gymmanagementsystem.service;

import org.example.gymmanagementsystem.enums.MembershipType;
import org.springframework.http.ResponseEntity;

public interface GymService {
    ResponseEntity<String> enterGym(Integer customerId);
    ResponseEntity<String> renewSubscription(Integer customerId, MembershipType newMembershipType);
    void startSubscription(Integer customerId);
    void stopSubscription(Integer customerId, Integer daysToPause);

}
