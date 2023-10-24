package com.eureka.demoapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eureka.demoapi.entities.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

}
