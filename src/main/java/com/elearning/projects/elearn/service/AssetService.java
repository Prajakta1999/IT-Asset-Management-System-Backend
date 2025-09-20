package com.elearning.projects.elearn.service;

import com.elearning.projects.elearn.dto.request.AssetCreateRequestDto;
import com.elearning.projects.elearn.dto.response.AssetResponseDto;
import com.elearning.projects.elearn.entity.enums.AssetStatus;
import com.elearning.projects.elearn.entity.enums.AssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AssetService {

    AssetResponseDto createAsset(AssetCreateRequestDto assetDto);

    AssetResponseDto getAssetById(Long id);

    Page<AssetResponseDto> getAllAssets(Pageable pageable, AssetType type, AssetStatus status, String employeeName, LocalDate startDate, LocalDate endDate);

    AssetResponseDto updateAsset(Long id, AssetCreateRequestDto assetDetails);

    void deleteAsset(Long id);
}
