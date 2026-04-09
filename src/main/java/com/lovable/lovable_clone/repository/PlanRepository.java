package com.lovable.lovable_clone.repository;

import com.lovable.lovable_clone.entity.Plan;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<@NonNull Plan,@NonNull Long> {
}
