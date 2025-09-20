package com.elearning.projects.elearn.dto.response;

import com.elearning.projects.elearn.entity.enums.AssetRequestStatus;
import com.elearning.projects.elearn.entity.enums.AssetType;
import java.time.LocalDateTime;

public record AssetRequestResponseDto(
    Long id,
    AssetType assetType,
    String justification,
    String preferredSpecifications,
    AssetRequestStatus status,
    String rejectionReason,
    UserSummaryDto requestedBy,
    UserSummaryDto reviewedBy,
    AssetResponseDto assignedAsset,
    LocalDateTime createdAt
) {}
