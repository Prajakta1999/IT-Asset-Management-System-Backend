package com.elearning.projects.elearn.dto.response;

import com.elearning.projects.elearn.entity.enums.AssetStatus;
import com.elearning.projects.elearn.entity.enums.AssetType;
import java.time.LocalDate;

public record AssetResponseDto(
    Long id,
    AssetType assetType,
    String manufacturer,
    String model,
    String serialNumber,
    LocalDate purchaseDate,
    LocalDate warrantyExpiry,
    AssetStatus status,
    UserSummaryDto assignedTo
) {}
