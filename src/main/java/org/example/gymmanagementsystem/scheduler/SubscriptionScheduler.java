package org.example.gymmanagementsystem.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.example.gymmanagementsystem.dao.repository.CustomersRepository;
import org.example.gymmanagementsystem.service.impl.EmailServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionScheduler {

    private final CustomersRepository customersRepository;
    private final EmailServiceImpl emailService;

    private boolean isSchedulerActive = true;

    @Scheduled(cron = "0  0  0 * * ?")
    @Transactional
    public void checkAndUpdateSubscriptions() {
        if (!isSchedulerActive) {
            log.info("Scheduler is paused.");
            return;
        } else {
            log.info("Scheduler is running.");
        }

        LocalDateTime today = LocalDateTime.now();
        log.info("🔍 [{}] Checking expired subscriptions...", today);

        List<CustomerEntity> customers = customersRepository.findAll();

        for (CustomerEntity customer : customers) {
            LocalDateTime subscriptionEndDate = customer.getSubscriptionEndDate();

            log.info(" Checking customer name: {}, Customer surname: {}, Subscription End Date: {}, Active: {}",
                    customer.getName(),customer.getSurname(),
                    subscriptionEndDate, customer.getIsActive());

            if (subscriptionEndDate.isBefore(today) && customer.getIsActive()) {
                log.warn(" [{}] Expired subscription found for: {}, Setting inactive.", today, customer.getEmail());
                customer.setRemainingEntries(0);
                customer.setIsActive(false);
                customersRepository.saveAndFlush(customer);

                log.info("[{}] {} marked as inactive.", today, customer.getEmail());

                if (customer.getEmail() != null && !customer.getEmail().isBlank()) {
                    sendExpirationEmail(customer);
                    log.info("[{}] Expiration email sent to: {}", today, customer.getEmail());
                }
            }
        }
    }

    public void stopScheduler() {
        isSchedulerActive = false;
        log.info("Scheduler has been stopped.");
    }

    public void startScheduler() {
        isSchedulerActive = true;
        log.info("Scheduler has been started.");
    }

    private void sendExpirationEmail(CustomerEntity customer) {
        emailService.sendEmail(
                customer.getEmail(),
                "Your Gym Membership Has Ended",
                "Hello " + customer.getName() + ",\n\n" +
                        "Your gym membership has now ended. If you would like to renew your membership, please contact us.\n\n" +
                        "Best regards,\n" +
                        "Alicanov Fitness Team"
        );
    }
}
