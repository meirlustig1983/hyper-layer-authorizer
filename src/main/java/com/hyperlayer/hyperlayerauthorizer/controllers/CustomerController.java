package com.hyperlayer.hyperlayerauthorizer.controllers;

import com.hyperlayer.hyperlayerauthorizer.dto.Customer;
import com.hyperlayer.hyperlayerauthorizer.services.CustomerService;
import com.hyperlayer.hyperlayerauthorizer.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        Customer customer = service.findById(customerId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer newCustomer = service.save(customer);
        if (newCustomer != null) {
            return ResponseEntity.created(ControllerHelper.getLocation()).body(newCustomer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}