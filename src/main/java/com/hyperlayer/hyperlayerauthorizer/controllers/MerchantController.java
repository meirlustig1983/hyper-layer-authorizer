package com.hyperlayer.hyperlayerauthorizer.controllers;

import com.hyperlayer.hyperlayerauthorizer.dto.Merchant;
import com.hyperlayer.hyperlayerauthorizer.services.MerchantService;
import com.hyperlayer.hyperlayerauthorizer.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService service;

    @Autowired
    public MerchantController(MerchantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{merchantId}")
    public ResponseEntity<Merchant> getCustomerById(@PathVariable String merchantId) {
        Merchant merchant = service.findById(merchantId);
        if (merchant != null) {
            return ResponseEntity.ok(merchant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Merchant> createMerchant(@RequestBody Merchant merchant) {
        Merchant newMerchant = service.save(merchant);
        if (newMerchant != null) {
            return ResponseEntity.created(ControllerHelper.getLocation("/api/merchants")).body(newMerchant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
