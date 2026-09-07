package com.codifyai.serviceimpl;

import com.codifyai.dto.subscription.PlanLimitResponse;
import com.codifyai.dto.subscription.UsageTodayResponse;
import com.codifyai.service.UsageService;
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
