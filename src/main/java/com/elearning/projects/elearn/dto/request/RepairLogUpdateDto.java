package com.elearning.projects.elearn.dto.request;

import com.elearning.projects.elearn.entity.enums.RepairStatus;
import jakarta.validation.constraints.NotNull;

public record RepairLogUpdateDto(
    @NotNull(message = "Repair status is required")
    RepairStatus status
) {}
