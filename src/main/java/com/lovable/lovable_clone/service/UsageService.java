package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.subscription.PlanLimitResponse;
import com.lovable.lovable_clone.dto.subscription.UsageTodayResponse;
import org.springframework.stereotype.Service;

public interface UsageService {
    UsageTodayResponse getTodayUsage(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimitOfUser(Long userId);
}
