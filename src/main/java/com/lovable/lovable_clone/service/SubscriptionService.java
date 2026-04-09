package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.subscription.*;

import java.util.List;

public interface SubscriptionService {
    List<PlanResponse> getAllActivePlans();

    SubscriptionReponse getMySubscription(Long userId);
}
