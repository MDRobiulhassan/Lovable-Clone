package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.subscription.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BillingService {
    List<PlanResponse> getAllActivePlans();

    SubscriptionReponse getMySubscription(Long userId);

    CheckoutResponse createCheckoutSessionUrl(Long userId, CheckoutRequest request);

    PortalResponse openCustomerPortal(Long userId);
}
