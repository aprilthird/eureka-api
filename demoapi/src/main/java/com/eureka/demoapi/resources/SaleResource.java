package com.eureka.demoapi.resources;

import java.net.URI;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eureka.demoapi.entities.Customer;
import com.eureka.demoapi.entities.Product;
import com.eureka.demoapi.entities.Sale;
import com.eureka.demoapi.entities.User;
import com.eureka.demoapi.exceptions.EntityNotFoundException;
import com.eureka.demoapi.payload.request.SaleRequest;
import com.eureka.demoapi.repositories.CustomerRepository;
import com.eureka.demoapi.repositories.ProductRepository;
import com.eureka.demoapi.repositories.SaleRepository;
import com.eureka.demoapi.repositories.UserRepository;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/sales")
@PreAuthorize("hasRole('USER')")
public class SaleResource {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SaleRepository saleRepository;

    @GetMapping()
    public List<Sale> retrieveAll(Principal principal, @RequestParam(required = false) String q) {
        Optional<User> currentUser = userRepository.findByUsername(principal.getName());
        List<Sale> results = saleRepository.findAll().stream()
                .filter(x -> x.getUser().getId() == currentUser.get().getId()).toList();
        if (q != null && q != "") {
            results = results
                    .stream().filter(x -> x.getProduct().getName().contains(q)
                            || x.getProduct().getDescription().contains(q) || x.getCustomer().getName().contains(q))
                    .toList();
        }
        return results;
    }

    @GetMapping("/{id}")
    public Sale retrieve(@PathVariable long id) {
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty())
            throw new EntityNotFoundException("id-" + id);

        return sale.get();
    }

    @PostMapping()
    public ResponseEntity<?> create(Principal principal, @Valid @RequestBody SaleRequest sale) {
        Optional<User> currentUser = userRepository.findByUsername(principal.getName());
        Optional<Product> product = productRepository.findById(sale.getProductId());
        Optional<Customer> customer = customerRepository.findById(sale.getCustomerId());

        Sale newSale = new Sale(sale.getQuantity(), product.get().getPrice(), Instant.now(), sale.getSaleDate(),
                product.get(),
                customer.get(),
                currentUser.get());

        Sale savedSale = saleRepository.save(newSale);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSale.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody SaleRequest sale, @PathVariable long id) {
        Optional<Sale> existingSale = saleRepository.findById(id);

        if (existingSale.isEmpty())
            return ResponseEntity.notFound().build();

        Optional<Product> product = productRepository.findById(sale.getProductId());

        if (product.isEmpty())
            throw new EntityNotFoundException("id-" + id);

        Optional<Customer> customer = customerRepository.findById(sale.getCustomerId());

        if (customer.isEmpty())
            throw new EntityNotFoundException("id-" + id);

        Sale savedSale = existingSale.get();
        savedSale.setQuantity(sale.getQuantity());
        savedSale.setSaleDate(sale.getSaleDate());
        savedSale.setProduct(product.get());
        savedSale.setUnitPrice(product.get().getPrice());
        savedSale.setCustomer(customer.get());
        saleRepository.save(savedSale);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Sale> existingSale = saleRepository.findById(id);

        if (existingSale.isEmpty())
            return ResponseEntity.notFound().build();

        saleRepository.delete(existingSale.get());

        return ResponseEntity.noContent().build();
    }
}
