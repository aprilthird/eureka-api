package com.eureka.demoapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eureka.demoapi.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
