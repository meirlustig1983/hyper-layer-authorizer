package com.hyperlayer.hyperlayerauthorizer.controllers;

import com.hyperlayer.hyperlayerauthorizer.controllers.requests.ShareRewardRequest;
import com.hyperlayer.hyperlayerauthorizer.controllers.requests.TransactionAuthorizationRequest;
import com.hyperlayer.hyperlayerauthorizer.dto.Customer;
import com.hyperlayer.hyperlayerauthorizer.services.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/management")
public class ManagementController {

    private final ManagementService service;

    @Autowired
    public ManagementController(ManagementService service) {
        this.service = service;
    }

    @PostMapping("/share-reward")
    public ResponseEntity<Customer> shareReward(@RequestBody ShareRewardRequest request) {
        Customer customer = service.shareReward(request);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/authorize")
    public ResponseEntity<Boolean> authorize(@RequestBody TransactionAuthorizationRequest request) {
        return ResponseEntity.ok(service.authorize(request));
    }
}
