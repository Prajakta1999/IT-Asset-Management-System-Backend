package com.elearning.projects.elearn.dto.request;

import com.elearning.projects.elearn.entity.enums.AssetRequestStatus;
import jakarta.validation.constraints.NotNull;

public record AssetRequestReviewDto(
    @NotNull(message = "Review status is required")
    AssetRequestStatus status, // Must be APPROVED or REJECTED

    Long assetIdToAssign, // Required if status is APPROVED

    String rejectionReason // Required if status is REJECTED
) {}
