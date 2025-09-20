package com.elearning.projects.elearn.service;

import com.elearning.projects.elearn.dto.request.AssetRequestCreateDto;
import com.elearning.projects.elearn.dto.request.AssetRequestReviewDto;
import com.elearning.projects.elearn.dto.response.AssetRequestResponseDto;

import java.util.List;

public interface AssetRequestService {
    AssetRequestResponseDto createAssetRequest(AssetRequestCreateDto requestDto, String username);

    List<AssetRequestResponseDto> getMyAssetRequests(String username);

    List<AssetRequestResponseDto> getAllPendingRequests();

    AssetRequestResponseDto reviewAssetRequest(Long requestId, AssetRequestReviewDto reviewDto, String adminUsername);
}
