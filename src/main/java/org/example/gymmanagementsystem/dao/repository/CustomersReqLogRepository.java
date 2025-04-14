package org.example.gymmanagementsystem.dao.repository;

import org.example.gymmanagementsystem.dao.entity.CustomerReqLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomersReqLogRepository extends JpaRepository<CustomerReqLogEntity, Integer> {

    @Query("SELECT c FROM CustomerReqLogEntity c WHERE c.customerEntity.id = :customerId AND c.deleted = false")
    CustomerReqLogEntity findByCustomersEntity_Id(@Param("customerId") Integer customerId);


}
