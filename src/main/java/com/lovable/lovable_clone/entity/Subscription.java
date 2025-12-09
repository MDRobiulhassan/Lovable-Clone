package com.lovable.lovable_clone.entity;

import com.lovable.lovable_clone.enums.SubscriptionStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {
    Long id;
    User user;
    Plan plan;
    String stripeSubscriptionId;
    SubscriptionStatus status;
    Instant currentPeriodStart;
    Boolean currentPeriodEnd;
    Instant createdAt;
    Instant updatedAt;
}
