package com.codifyai.service;

import com.codifyai.dto.subscription.PlanResponse;
import com.codifyai.dto.subscription.SubscriptionReponse;
import com.codifyai.enums.SubscriptionStatus;

import java.time.Instant;
import java.util.List;

public interface SubscriptionService {
    List<PlanResponse> getAllActivePlans();

    SubscriptionReponse getMySubscription();

    //    void activateSubscription(Long userId, Long planId, String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd);
    void activateSubscription(Long userId, Long planId, String subscriptionId,
                              SubscriptionStatus status, Instant periodStart, Instant periodEnd);

    void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String gatewaySubscriptionId);

    void renewSubscriptionPeriod(String subscriptionId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subId);

    boolean CanCreateNewProject();
}
