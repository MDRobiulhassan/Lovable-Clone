package com.lovable.lovable_clone.service.impl;

import com.lovable.lovable_clone.dto.subscription.PlanLimitResponse;
import com.lovable.lovable_clone.dto.subscription.UsageTodayResponse;
import com.lovable.lovable_clone.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {

    @Override
    public UsageTodayResponse getTodayUsage(Long userId) {
        return null;
    }

    @Override
    public PlanLimitResponse getCurrentSubscriptionLimitOfUser(Long userId) {
        return null;
    }
}
