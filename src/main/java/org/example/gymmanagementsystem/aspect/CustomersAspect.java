package org.example.gymmanagementsystem.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.example.gymmanagementsystem.dao.entity.CustomerReqLogEntity;
import org.example.gymmanagementsystem.dao.repository.CustomersReqLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomersAspect {

    private final CustomersReqLogRepository customersReqLogRepository;

    @AfterReturning(pointcut = "execution(* org.example.gymmanagementsystem.dao.repository.*.save(..))")
    public void logRegisterCustomer(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];

        if (entity instanceof CustomerEntity customerEntity) {
            CustomerReqLogEntity existingLog = customersReqLogRepository.findByCustomersEntity_Id(customerEntity.getId());

            if (existingLog != null) {
                if (existingLog.getDeleted()) {
                    log.info("Re-activating soft deleted log for customer ID: {}", customerEntity.getId());
                    existingLog.setDeleted(false);
                } else {
                    log.warn("Log already exists for customer ID: {}. Updating existing log...", customerEntity.getId());
                }

                existingLog.setRegDate(LocalDateTime.now());
                existingLog.setMethodName(joinPoint.getSignature().getName());
                customersReqLogRepository.save(existingLog);
            } else {
                log.info("Creating new log for customer ID: {}", customerEntity.getId());

                CustomerReqLogEntity customerReqLogEntity = new CustomerReqLogEntity();
                customerReqLogEntity.setRegDate(LocalDateTime.now());
                customerReqLogEntity.setMethodName(joinPoint.getSignature().getName());
                customerReqLogEntity.setCustomerEntity(customerEntity);
                customerReqLogEntity.setDeleted(false);
                customersReqLogRepository.save(customerReqLogEntity);
            }
        }
    }


}
