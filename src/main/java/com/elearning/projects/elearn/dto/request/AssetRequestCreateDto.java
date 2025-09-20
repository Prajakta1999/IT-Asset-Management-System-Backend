package com.elearning.projects.elearn.dto.request;

import com.elearning.projects.elearn.entity.enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssetRequestCreateDto(
    @NotNull(message = "Asset type is required")
    AssetType assetType,

    @NotBlank(message = "Justification is required")
    String justification,

    String preferredSpecifications
) {}
