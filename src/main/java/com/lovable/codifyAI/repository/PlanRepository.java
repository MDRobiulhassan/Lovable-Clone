package com.lovable.codifyAI.repository;

import com.lovable.codifyAI.entity.Plan;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<@NonNull Plan,@NonNull Long> {
    Optional<Plan> findByStripePriceId(String id);
}
