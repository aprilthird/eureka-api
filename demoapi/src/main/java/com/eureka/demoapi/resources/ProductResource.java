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
import com.eureka.demoapi.entities.Product;
import com.eureka.demoapi.exceptions.EntityNotFoundException;
import com.eureka.demoapi.payload.request.ProductRequest;
import com.eureka.demoapi.repositories.ProductRepository;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
@PreAuthorize("hasRole('USER')")
public class ProductResource {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public List<Product> retrieveAll(@RequestParam(required = false) String q) {
        List<Product> results = productRepository.findAll();
        if (q != null && q != "") {
            results = results.stream().filter(x -> x.getName().contains(q) || x.getDescription().contains(q)).toList();
        }
        return results;
    }

    @GetMapping("/{id}")
    public Product retrieve(@PathVariable long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty())
            throw new EntityNotFoundException("id-" + id);

        return product.get();
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequest product) {

        Product newProduct = new Product(product.getName(), product.getDescription(), product.getPrice());

        Product savedProduct = productRepository.save(newProduct);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ProductRequest product, @PathVariable long id) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isEmpty())
            return ResponseEntity.notFound().build();

        Product savedProduct = existingProduct.get();
        savedProduct.setName(product.getName());
        savedProduct.setDescription(product.getDescription());
        savedProduct.setPrice(product.getPrice());
        productRepository.save(savedProduct);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isEmpty())
            return ResponseEntity.notFound().build();

        productRepository.delete(existingProduct.get());

        return ResponseEntity.noContent().build();
    }
}