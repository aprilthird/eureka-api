package com.eureka.demoapi.resources;

import java.net.URI;
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
import com.eureka.demoapi.exceptions.EntityNotFoundException;
import com.eureka.demoapi.payload.request.CustomerRequest;
import com.eureka.demoapi.repositories.CustomerRepository;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
@PreAuthorize("hasRole('USER')")
public class CustomerResource {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping()
    public List<Customer> retrieveAll(@RequestParam(required = false) String q) {
        List<Customer> results = customerRepository.findAll();
        if (q != null && q != "") {
            results = results.stream().filter(x -> x.getName().contains(q)).toList();
        }
        return results;
    }

    @GetMapping("/{id}")
    public Customer retrieve(@PathVariable long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty())
            throw new EntityNotFoundException("id-" + id);

        return customer.get();
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody CustomerRequest customer) {

        Customer newCustomer = new Customer(customer.getName());

        Customer savedCustomer = customerRepository.save(newCustomer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CustomerRequest customer, @PathVariable long id) {
        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isEmpty())
            return ResponseEntity.notFound().build();

        Customer savedCustomer = existingCustomer.get();
        savedCustomer.setName(customer.getName());
        customerRepository.save(savedCustomer);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isEmpty())
            return ResponseEntity.notFound().build();

        customerRepository.delete(existingCustomer.get());

        return ResponseEntity.noContent().build();
    }
}
