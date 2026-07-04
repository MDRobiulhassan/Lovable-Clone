package com.lovable.codifyAI.mapper;

import com.lovable.codifyAI.dto.subscription.PlanResponse;
import com.lovable.codifyAI.dto.subscription.SubscriptionReponse;
import com.lovable.codifyAI.entity.Plan;
import com.lovable.codifyAI.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionReponse toSubscriptionResponse(Subscription subscription);
    PlanResponse toPlanResponse(Plan plan);
}
