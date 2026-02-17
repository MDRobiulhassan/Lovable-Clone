package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.subscription.PlanLimitResponse;
import com.lovable.lovable_clone.dto.subscription.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsage(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimitOfUser(Long userId);
}
