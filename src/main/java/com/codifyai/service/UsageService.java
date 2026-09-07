package com.codifyai.service;

import com.codifyai.dto.subscription.PlanLimitResponse;
import com.codifyai.dto.subscription.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsage(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimitOfUser(Long userId);
}
