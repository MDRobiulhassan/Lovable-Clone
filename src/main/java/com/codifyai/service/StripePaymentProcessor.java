package com.codifyai.service;

import com.codifyai.dto.subscription.CheckoutRequest;
import com.codifyai.dto.subscription.CheckoutResponse;
import com.codifyai.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface StripePaymentProcessor {
    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metaData);
}
