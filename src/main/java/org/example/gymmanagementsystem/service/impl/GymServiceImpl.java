package org.example.gymmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.example.gymmanagementsystem.dao.entity.GymEntryLogEntity;
import org.example.gymmanagementsystem.dao.repository.CustomersRepository;
import org.example.gymmanagementsystem.dao.repository.GymEntryLogRepository;
import org.example.gymmanagementsystem.enums.MembershipType;
import org.example.gymmanagementsystem.exceptions.NotFoundException;
import org.example.gymmanagementsystem.exceptions.ValidationException;
import org.example.gymmanagementsystem.scheduler.SubscriptionScheduler;
import org.example.gymmanagementsystem.service.GymService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {

    private final CustomersRepository customersRepository;

    private final EmailServiceImpl emailService;
    private final GymEntryLogRepository gymEntryLogRepository;
    private final SubscriptionScheduler subscriptionScheduler;


    @Override
    @Transactional
    public ResponseEntity<String> enterGym(Integer customerId) {
        Optional<CustomerEntity> optionalCustomer = customersRepository.findById(customerId);
        log.info("Searching for customer with id {}", customerId);

        if (optionalCustomer.isEmpty()) {
            log.warn("Customer with id {} not found.", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found.");
        }

        CustomerEntity customer = optionalCustomer.get();
        LocalDateTime today = LocalDateTime.now();
        String customerName = customer.getName() + " " + customer.getSurname();
        log.info("Customer found: {}", customerName);

        if (customer.getSubscriptionEndDate().isBefore(today)) {
            customer.setIsActive(false);
            subscriptionScheduler.stopScheduler();
            customersRepository.save(customer);

            log.info("Subscription expired for customer {}. Deactivating and stopping scheduler.", customerName);

            if (customer.getEmail() != null && !customer.getEmail().isBlank()) {
                sendEarlyExpirationEmail(customer);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(" " + customerName + ", your subscription has expired. Please renew your membership.");
        }

        if (customer.getRemainingEntries() <= 0) {
            customer.setIsActive(false);
            customersRepository.save(customer);

            log.info("Customer {} has used all their entries. Deactivating account.", customerName);

            if (customer.getEmail() != null && !customer.getEmail().isBlank()) {
                subscriptionScheduler.stopScheduler();
                sendEarlyExpirationEmail(customer);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(" " + customerName + ", you have used all your entries for this month. Please renew your membership.");
        }

        customer.setRemainingEntries(customer.getRemainingEntries() - 1);
        log.info("Customer {} entered the gym. Remaining entries: {}", customerName, customer.getRemainingEntries());

        if (customer.getRemainingEntries() == 0) {
            customer.setIsActive(false);
            subscriptionScheduler.stopScheduler();
            customersRepository.save(customer);

            log.info("Customer {} has no remaining entries. Deactivating and stopping scheduler.", customerName);

            if (customer.getEmail() != null && !customer.getEmail().isBlank()) {
                sendEarlyExpirationEmail(customer);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(" " + customerName + ", you have used all your entries for this month. Please renew your membership.");
        }

        customersRepository.save(customer);

        GymEntryLogEntity entryLog = new GymEntryLogEntity();
        entryLog.setCustomer(customer);
        entryLog.setEntryTime(LocalDateTime.now());

        gymEntryLogRepository.save(entryLog);
        log.info("Gym entry logged for customer {} at {}", customerName, entryLog.getEntryTime());

        return ResponseEntity.ok(customerName + " has entered the gym. Remaining entries: " + customer.getRemainingEntries());
    }


    private void sendEarlyExpirationEmail(CustomerEntity customer) {
        emailService.sendEmail(
                customer.getEmail(),
                "Your Gym Membership Has Ended Early",
                "Hello " + customer.getName() + ",\n\n" +
                        "We noticed that you have used all your entries before the end of the month.\n\n" +
                        "If you would like to continue training, please renew your membership.\n\n" +
                        "Best regards,\n" +
                        "Alicanov Fitness Team"
        );

        log.info(" [{}] Early expiration email sent to: {}", LocalDate.now(), customer.getEmail());
    }


    @Override
    @Transactional
    public ResponseEntity<String> renewSubscription(Integer customerId, MembershipType newMembershipType) {
        Optional<CustomerEntity> optionalCustomer = customersRepository.findById(customerId);
        log.info("Attempting to renew subscription for customer with id {}", customerId);

        if (optionalCustomer.isEmpty()) {
            log.warn("Customer with id {} not found for subscription renewal.", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found.");
        }

        CustomerEntity customer = optionalCustomer.get();
        LocalDateTime today = LocalDateTime.now();

        if (customer.getIsActive()) {
            log.info("Customer {} is already active. No need for renewal.", customer.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Customer is already active. No need to renew.");
        }

        if (customer.getSubscriptionEndDate().isBefore(today) || customer.getRemainingEntries() <= 0) {
            log.info("Customer {} subscription expired or no remaining entries. Renewing subscription.", customer.getName());

            MembershipType updatedMembershipType = newMembershipType != null ? newMembershipType : customer.getMembershipType();
            customer.setMembershipType(updatedMembershipType);

            int newEntries = updatedMembershipType.getMonthlyEntries();
            customer.setRemainingEntries(newEntries);

            customer.setSubscriptionStartDate(today);
            customer.setSubscriptionEndDate(today.plusMinutes(1));

            customer.setIsActive(true);

            customersRepository.save(customer);

            subscriptionScheduler.startScheduler();

            return ResponseEntity.ok("Subscription renewed with " + updatedMembershipType.name() +
                    ". New expiration date: " + customer.getSubscriptionEndDate() +
                    ". Remaining entries: " + customer.getRemainingEntries());
        }

        log.info("Customer {} subscription is still valid. No need to renew yet.", customer.getName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Subscription is still valid. No need to renew yet.");
    }

    @Override
    public void stopSubscription(Integer customerId, Integer daysToPause) {
        Optional<CustomerEntity> customerOpt = customersRepository.findById(customerId);
        log.info("Attempting to stop subscription for customer with id {} for {} days.", customerId, daysToPause);

        if (customerOpt.isEmpty()) {
            log.error("Stop subscription failed. Customer with ID {} not found.", customerId);
            throw new NotFoundException("Customer with ID " + customerId + " not found");
        }

        CustomerEntity customer = customerOpt.get();

        if (daysToPause > 7) {
            log.error("Customer {} attempted to pause subscription for more than 7 days.", customerId);
            throw new ValidationException("You cannot pause the subscription for more than 7 days.");
        }

        if (customer.getIsActive()) {
            log.debug("Customer {} subscription is active. Pausing for {} days.", customerId, daysToPause);

            LocalDateTime currentEndDate = customer.getSubscriptionEndDate();
            if (currentEndDate == null) {
                currentEndDate = LocalDateTime.now();
            }

            LocalDateTime newEndDate = currentEndDate.plusDays(daysToPause);
            customer.setSubscriptionEndDate(newEndDate);

            customer.setIsActive(false);

            customersRepository.save(customer);

            log.info("Subscription stopped and paused for {} days. New end date: {}", customerId, newEndDate);

            scheduleSubscriptionActivation(customerId, newEndDate);
        } else {
            log.warn("Customer {} already has an inactive subscription.", customerId);
            throw new ValidationException("Customer subscription is already paused.");
        }
    }

    public void startSubscription(Integer customerId) {
        Optional<CustomerEntity> customerOpt = customersRepository.findById(customerId);

        if (customerOpt.isEmpty()) {
            log.error("Start subscription failed. Customer with ID {} not found.", customerId);
            throw new NotFoundException("Customer with ID " + customerId + " not found");
        }

        CustomerEntity customer = customerOpt.get();

        customer.setIsActive(true);

        LocalDateTime currentEndDate = LocalDateTime.now();
        customer.setSubscriptionEndDate(currentEndDate);

        customersRepository.save(customer);

        log.info("Subscription manually started for customer ID {}", customerId);
    }

    private void scheduleSubscriptionActivation(Integer customerId, LocalDateTime newEndDate) {

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(newEndDate) || now.isEqual(newEndDate)) {
            Optional<CustomerEntity> customerOpt = customersRepository.findById(customerId);
            customerOpt.ifPresent(customer -> {
                customer.setIsActive(true);
                customersRepository.save(customer);
                log.info("Subscription for customer ID {} has been automatically activated.", customerId);
            });
        }
    }

}


