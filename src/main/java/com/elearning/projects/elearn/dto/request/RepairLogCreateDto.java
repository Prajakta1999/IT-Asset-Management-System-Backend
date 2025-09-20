package com.elearning.projects.elearn.dto.request;

import com.elearning.projects.elearn.entity.enums.IssueType;
import com.elearning.projects.elearn.entity.enums.UrgencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RepairLogCreateDto(
    @NotNull(message = "Asset ID is required")
    Long assetId,

    @NotNull(message = "Issue type is required")
    IssueType issueType,

    @NotBlank(message = "Description is required")
    String description,

    @NotNull(message = "Urgency level is required")
    UrgencyLevel urgency
) {}
