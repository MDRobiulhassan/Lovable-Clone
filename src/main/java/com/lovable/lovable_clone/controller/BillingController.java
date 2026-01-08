package com.lovable.lovable_clone.controller;

import com.lovable.lovable_clone.dto.subscription.CheckoutRequest;
import com.lovable.lovable_clone.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/plans")
    public ResponseEntity<?> getAllPlans() {
        return ResponseEntity.ok(billingService.getAllActivePlans());
    }

    @GetMapping("/me/subscription")
    public ResponseEntity<?> getMySubscription() {
        Long userId = 1L;
        return ResponseEntity.ok(billingService.getMySubscription(userId));
    }

    @PostMapping("/stripe/checkout")
    public ResponseEntity<?> createCheckoutResponse(@RequestBody CheckoutRequest request) {
        Long userId = 1L;
        return ResponseEntity.ok(billingService.createCheckoutSessionUrl(userId, request));
    }

    @PostMapping("/stripe/portal")
    public ResponseEntity<?> openCustomerPortal() {
        Long userId = 1L;
        return ResponseEntity.ok(billingService.openCustomerPortal(userId));
    }
}
