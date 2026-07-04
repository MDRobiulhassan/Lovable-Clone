package com.lovable.codifyAI.service;

import com.lovable.codifyAI.dto.subscription.*;
import com.lovable.codifyAI.enums.SubscriptionStatus;

import java.time.Instant;
import java.util.List;

public interface SubscriptionService {
    List<PlanResponse> getAllActivePlans();

    SubscriptionReponse getMySubscription();

    void activateSubscription(Long userId, Long planId, String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd);

    void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String subscriptionId);

    void renewSubscriptionPeriod(String subscriptionId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subId);
}
