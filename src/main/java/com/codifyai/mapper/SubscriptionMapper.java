package com.codifyai.mapper;

import com.codifyai.dto.subscription.PlanResponse;
import com.codifyai.dto.subscription.SubscriptionReponse;
import com.codifyai.entity.Plan;
import com.codifyai.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionReponse toSubscriptionResponse(Subscription subscription);
    PlanResponse toPlanResponse(Plan plan);
}
