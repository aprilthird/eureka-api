package com.eureka.demoapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eureka.demoapi.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
