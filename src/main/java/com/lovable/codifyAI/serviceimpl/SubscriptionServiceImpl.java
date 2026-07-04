package com.lovable.codifyAI.serviceimpl;

import com.lovable.codifyAI.dto.subscription.PlanResponse;
import com.lovable.codifyAI.dto.subscription.SubscriptionReponse;
import com.lovable.codifyAI.entity.Plan;
import com.lovable.codifyAI.entity.Subscription;
import com.lovable.codifyAI.entity.User;
import com.lovable.codifyAI.enums.SubscriptionStatus;
import com.lovable.codifyAI.error.ResourceNotFoundException;
import com.lovable.codifyAI.mapper.SubscriptionMapper;
import com.lovable.codifyAI.repository.PlanRepository;
import com.lovable.codifyAI.repository.SubscriptionRepository;
import com.lovable.codifyAI.repository.UserRepository;
import com.lovable.codifyAI.security.AuthUtil;
import com.lovable.codifyAI.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @Override
    public List<PlanResponse> getAllActivePlans() {
        return List.of();
    }

    @Override
    public SubscriptionReponse getMySubscription() {
        Long userId = authUtil.getCurrentUserId();

        var currentSubscription = subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE, SubscriptionStatus.TRAILING)).orElse(new Subscription());

        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

//    @Override
//    public void activateSubscription(Long userId, Long planId, String subscriptionId) {
//        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
//        if (exists) return;
//
//        User user = getUser(userId);
//        Plan plan = getPlan(planId);
//
//        Subscription subscription = Subscription.builder()
//                .user(user)
//                .plan(plan)
//                .stripeSubscriptionId(subscriptionId)
//                .status(SubscriptionStatus.INCOMPLETE)
//                .build();
//        subscriptionRepository.save(subscription);
//    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd) {

        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if (exists) {
            return;
        }

        User user = getUser(userId);
        Plan plan = getPlan(planId);

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(status)
                .currentPeriodStart(periodStart)
                .currentPeriodEnd(periodEnd)
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    public void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

    }

    @Override
    public void cancelSubscription(String subscriptionId) {

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if (subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscriptionRepository.save(subscription);
    }


    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptionId) {

    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User ", userId.toString()));
    }

    private Plan getPlan(Long planId) {
        return planRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("Plan", planId.toString()));
    }

    private Subscription getSubscription(String gatewaySubscriptionId) {
        return subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", gatewaySubscriptionId));
    }
}
