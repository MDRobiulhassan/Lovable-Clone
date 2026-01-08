package com.lovable.lovable_clone.dto.subscription;

public record UsageTodayResponse(
        Integer tokenUsed,
        Integer tokenLimits,
        Integer previewsRunning,
        Integer previewsLimit
) {
}
