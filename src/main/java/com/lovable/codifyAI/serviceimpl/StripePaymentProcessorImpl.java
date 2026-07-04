package com.lovable.codifyAI.serviceimpl;

import com.lovable.codifyAI.dto.subscription.CheckoutRequest;
import com.lovable.codifyAI.dto.subscription.CheckoutResponse;
import com.lovable.codifyAI.dto.subscription.PortalResponse;
import com.lovable.codifyAI.entity.Plan;
import com.lovable.codifyAI.entity.User;
import com.lovable.codifyAI.enums.SubscriptionStatus;
import com.lovable.codifyAI.error.ResourceNotFoundException;
import com.lovable.codifyAI.repository.PlanRepository;
import com.lovable.codifyAI.repository.UserRepository;
import com.lovable.codifyAI.security.AuthUtil;
import com.lovable.codifyAI.service.StripePaymentProcessor;
import com.lovable.codifyAI.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentProcessorImpl implements StripePaymentProcessor {

    private final AuthUtil authUtil;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Value("${client.url}")
    private String frontendUrl;

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request) {
        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan",
                        String.valueOf(request.planId())
                ));

        Long userId = authUtil.getCurrentUserId();
        User user = getUser(userId);
        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build()
                )
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        SessionCreateParams.SubscriptionData.builder()
                                .setBillingMode(
                                        SessionCreateParams.SubscriptionData.BillingMode.builder()
                                                .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                                .build()
                                )
                                .build()
                )
                .setSuccessUrl(frontendUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id", plan.getId().toString());

        try {
            String stripeCustomerId = user.getStripeCustomerId();
            if (stripeCustomerId == null || stripeCustomerId.isEmpty()) {
                params.setCustomerEmail(user.getUsername());
            } else {
                params.setCustomer(stripeCustomerId);
            }


            Session session = Session.create(params.build());
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal() {
        return null;
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metaData) {
        log.debug("Handling Stripe Event {}", type);

        switch (type) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted((Session) stripeObject, metaData);
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject);
            case "customer.subscription.deleted" -> handleCustomerSubscriptionDeleted((Subscription) stripeObject);
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject);
            case "invoice.payment_failed" -> handleInvoicePaymentFailed((Invoice) stripeObject);
            default -> log.debug("Ignoring unhandled Stripe event type: {}", type);
        }
    }

//    private void handleCheckoutSessionCompleted(Session session, Map<String, String> metaData) {
//
//        if (session == null) {
//            log.error("Stripe session is null");
//            return;
//        }
//
//        Long userId = Long.parseLong(metaData.get("user_id"));
//        Long planId = Long.parseLong(metaData.get("plan_id"));
//
//        String subscriptionId = session.getSubscription();
//        String customerId = session.getCustomer();
//
//        User user = getUser(userId);
//        if (user.getStripeCustomerId() == null) {
//            user.setStripeCustomerId(customerId);
//            userRepository.save(user);
//        }
//
//        subscriptionService.activateSubscription(userId, planId, subscriptionId);
//    }

    private void handleCheckoutSessionCompleted(Session session, Map<String, String> metaData) {

        if (session == null) {
            log.error("Stripe session is null");
            return;
        }

        Long userId = Long.parseLong(metaData.get("user_id"));
        Long planId = Long.parseLong(metaData.get("plan_id"));

        String subscriptionId = session.getSubscription();
        String customerId = session.getCustomer();

        User user = getUser(userId);
        if (user.getStripeCustomerId() == null) {
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
        }

        try {
            Subscription stripeSubscription = Subscription.retrieve(subscriptionId);

            SubscriptionItem item = stripeSubscription.getItems().getData().get(0);

            SubscriptionStatus status = mapStripeStatusToEnum(stripeSubscription.getStatus());

            Instant periodStart = toInstant(item.getCurrentPeriodStart());
            Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

            log.info("Stripe Subscription JSON:\n{}", stripeSubscription.toJson());
            log.info("Period Start: {}", periodStart);
            log.info("Period End: {}", periodEnd);

            subscriptionService.activateSubscription(
                    userId,
                    planId,
                    subscriptionId,
                    status,
                    periodStart,
                    periodEnd
            );

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription) {

        if (subscription == null) {
            log.error("Subscription session is null");
            return;
        }

        SubscriptionStatus status = mapStripeStatusToEnum(subscription.getStatus());
        if (status == null) {
            log.warn("Subscription status is null inside handleCustomerSubscriptionUpdated");
            return;
        }

        SubscriptionItem item = subscription.getItems().getData().get(0);
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

        Long planId = resolvePlanId(item.getPrice());

        subscriptionService.updateSubscription(subscription.getId(), status, periodStart, periodEnd, subscription.getCancelAtPeriodEnd(), planId);
    }

    private void handleCustomerSubscriptionDeleted(Subscription subscription) {
        if (subscription == null) {
            log.warn("Subscription status is null in handleCustomerSubscriptionDeleted");
            return;
        }
        subscriptionService.cancelSubscription(subscription.getId());
    }

    private void handleInvoicePaid(Invoice invoice) {
        String subId=extractSubscriptionId(invoice);
        if (subId == null) return;

        try {
            Subscription subscription = Subscription.retrieve(subId);
            var item = subscription.getItems().getData().get(0);

            Instant periodStart = toInstant(item.getCurrentPeriodStart());
            Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

            subscriptionService.renewSubscriptionPeriod(subscription.getId(), periodStart, periodEnd);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleInvoicePaymentFailed(Invoice invoice) {
        String subId=extractSubscriptionId(invoice);
        if(subId == null) return;

        subscriptionService.markSubscriptionPastDue(subId);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
    }

    private SubscriptionStatus mapStripeStatusToEnum(String status) {
        return switch (status) {
            case "active" -> SubscriptionStatus.ACTIVE;
            case "trialing" -> SubscriptionStatus.TRAILING;
            case "past_due", "unpaid", "paused", "incomplete_expired" -> SubscriptionStatus.PAST_DUE;
            case "canceled" -> SubscriptionStatus.CANCELED;
            case "incomplete" -> SubscriptionStatus.INCOMPLETE;
            default -> {
                log.warn("Unhandled Stripe status {}", status);
                yield null;
            }
        };
    }

    private Instant toInstant(Long epoch) {
        return epoch != null ? Instant.ofEpochSecond(epoch) : null;
    }

    private Long resolvePlanId(Price price) {
        if (price == null || price.getId() == null) return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElse(null);
    }

    private String extractSubscriptionId(Invoice invoice) {
        var parent = invoice.getParent();
        if (parent == null) return null;
        var subDetails = parent.getSubscriptionDetails();
        if (subDetails == null) return null;
        return subDetails.getSubscription();
    }

}
