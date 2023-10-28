package com.hyperlayer.hyperlayerauthorizer.controllers;

import com.hyperlayer.hyperlayerauthorizer.dto.Transaction;
import com.hyperlayer.hyperlayerauthorizer.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransaction() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(service.findAll(customerId));
    }

    @GetMapping("/customer/{customerId}/merchant/{merchantId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomerAndMerchant(@PathVariable String customerId,
                                                                                  @PathVariable String merchantId) {
        return ResponseEntity.ok(service.findAll(customerId, merchantId));
    }

    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<Void> deleteTransactionsByCustomerId(@PathVariable String customerId) {
        service.deleteAllByCustomerId(customerId);
        return ResponseEntity.ok().build();
    }
}