package com.lovable.lovable_clone.dto.subscription;

public record UsageTodayResponse(
        int tokenUsed,
        int tokenLimits,
        int previewsRunning,
        int previewsLimit
) {
}
