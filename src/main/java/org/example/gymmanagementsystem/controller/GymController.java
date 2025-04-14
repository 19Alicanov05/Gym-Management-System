package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gymmanagementsystem.enums.MembershipType;
import org.example.gymmanagementsystem.service.GymService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;

    @PatchMapping("/enter/{customerId}")
    @Operation(
            summary = "Enter Gym"
    )
    public ResponseEntity<String> enterGym(@PathVariable Integer customerId) {
        return gymService.enterGym(customerId);
    }

    @PatchMapping("/renew/{customerId}")
    @Operation(
            summary = "Renew Subscription"
    )
    public ResponseEntity<String> renewSubscription(
            @PathVariable Integer customerId,
            @RequestParam(required = false) MembershipType newMembershipType) {
        return gymService.renewSubscription(customerId, newMembershipType);
    }

    @Operation(summary = "Stop Subscription", description = "Stops a customer's subscription and extends the end date by a certain number of days (max 7).")
    @PostMapping("/stop/{customerId}")
    public ResponseEntity<String> stopSubscription(
            @PathVariable Integer customerId,
            @RequestParam int daysToPause) {
        gymService.stopSubscription(customerId, daysToPause);
        return ResponseEntity.ok("Subscription stopped successfully. New end date updated.");
    }

    @Operation(summary = "Start Subscription", description = "Manually starts a customer's subscription (prioritizes automatic activation).")
    @PostMapping("/start/{customerId}")
    public ResponseEntity<String> startSubscription(@PathVariable Integer customerId) {
        gymService.startSubscription(customerId);
        return ResponseEntity.ok("Subscription started successfully.");
    }
}
