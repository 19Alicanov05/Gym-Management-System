package org.example.gymmanagementsystem.dao.repository;

import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomersRepository extends JpaRepository<CustomerEntity,Integer>,JpaSpecificationExecutor<CustomerEntity> {


}
