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
import com.lovable.codifyAI.repository.ProjectMemberRepository;
import com.lovable.codifyAI.repository.SubscriptionRepository;
import com.lovable.codifyAI.repository.UserRepository;
import com.lovable.codifyAI.security.AuthUtil;
import com.lovable.codifyAI.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final Integer FREE_TIER_PROJECT_ALLOWED = 1;

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

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId,
                                     SubscriptionStatus status, Instant periodStart, Instant periodEnd) {
        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if (exists) return;

        User user = getUser(userId);
        Plan plan = getPlan(planId);

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(status != null ? status : SubscriptionStatus.INCOMPLETE)
                .currentPeriodStart(periodStart)
                .currentPeriodEnd(periodEnd)
                .build();
        subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        boolean hasSubscriptionUpdate = false;

        if (status != null && status != subscription.getStatus()) {
            subscription.setStatus(status);
            hasSubscriptionUpdate = true;
        }

        if (periodStart != null && !periodStart.equals(subscription.getCurrentPeriodStart())) {
            subscription.setCurrentPeriodStart(periodStart);
            hasSubscriptionUpdate = true;
        }

        if (periodEnd != null && !periodEnd.equals(subscription.getCurrentPeriodEnd())) {
            subscription.setCurrentPeriodEnd(periodEnd);
            hasSubscriptionUpdate = true;
        }

        if (cancelAtPeriodEnd != null && cancelAtPeriodEnd != subscription.getCancelAtPeriodEnd()) {
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            hasSubscriptionUpdate = true;
        }

        if (planId != null && !planId.equals(subscription.getPlan().getId())) {
            Plan plan = getPlan(planId);
            subscription.setPlan(plan);
            hasSubscriptionUpdate = true;
        }

        if (hasSubscriptionUpdate) {
            log.debug("Subscription {} updated successfully", gatewaySubscriptionId);
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void cancelSubscription(String gatewaySubscriptionId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);
        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);
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
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        if (subscription.getStatus() == SubscriptionStatus.PAST_DUE) {
            log.debug("Subscription {} is already marked as PAST_DUE", gatewaySubscriptionId);
            return;
        }

        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);
    }

    @Override
    public boolean CanCreateNewProject() {
        Long userId = authUtil.getCurrentUserId();
        SubscriptionReponse currentSubscription = getMySubscription();

        int countOfOwnedProjects = projectMemberRepository.countProjectOwnedByUser(userId);

        if (currentSubscription.plan() == null) {
            return countOfOwnedProjects < FREE_TIER_PROJECT_ALLOWED;
        }

        return countOfOwnedProjects < currentSubscription.plan().maxProjects();
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
