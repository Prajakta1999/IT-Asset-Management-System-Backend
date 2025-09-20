package com.elearning.projects.elearn.dto.response;

import com.elearning.projects.elearn.entity.enums.IssueType;
import com.elearning.projects.elearn.entity.enums.RepairStatus;
import com.elearning.projects.elearn.entity.enums.UrgencyLevel;
import java.time.LocalDateTime;

public record RepairLogResponseDto(
    Long id,
    Long assetId,
    String assetSerialNumber,
    UserSummaryDto reportedBy,
    IssueType issueType,
    String description,
    UrgencyLevel urgency,
    RepairStatus status,
    LocalDateTime reportedAt,
    LocalDateTime resolvedAt
) {}
