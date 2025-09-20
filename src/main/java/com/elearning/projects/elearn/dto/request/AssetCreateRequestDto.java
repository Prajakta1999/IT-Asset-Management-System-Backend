package com.elearning.projects.elearn.dto.request;

import com.elearning.projects.elearn.entity.enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AssetCreateRequestDto(
    @NotNull(message = "Asset type cannot be null")
    AssetType assetType,

    @NotBlank(message = "Manufacturer is required")
    String manufacturer,

    @NotBlank(message = "Model is required")
    String model,

    @NotBlank(message = "Serial number is required")
    String serialNumber,

    LocalDate purchaseDate,
    LocalDate warrantyExpiry
) {}
