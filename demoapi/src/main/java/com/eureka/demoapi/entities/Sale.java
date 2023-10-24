package com.eureka.demoapi.entities;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Double unitPrice;

    private Instant createDate;

    private LocalDate saleDate;

    @ManyToOne
    @JoinColumn(columnDefinition = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(columnDefinition = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(columnDefinition = "customer_id", nullable = false)
    private Customer customer;

    public Sale() {
        super();
    }

    public Sale(Long id, Integer quantity, Double unitPrice, Instant createDate, LocalDate saleDate, Product product,
            Customer customer,
            User user) {
        super();
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.createDate = createDate;
        this.saleDate = saleDate;
        this.product = product;
        this.customer = customer;
        this.user = user;
    }

    public Sale(Integer quantity, Double unitPrice, Instant createDate, LocalDate saleDate, Product product,
            Customer customer,
            User user) {
        super();
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.createDate = createDate;
        this.saleDate = saleDate;
        this.product = product;
        this.customer = customer;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
