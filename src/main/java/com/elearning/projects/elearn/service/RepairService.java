package com.elearning.projects.elearn.service;

import com.elearning.projects.elearn.dto.request.RepairLogCreateDto;
import com.elearning.projects.elearn.dto.request.RepairLogUpdateDto;
import com.elearning.projects.elearn.dto.response.RepairLogResponseDto;

import java.util.List;

public interface RepairService {
    RepairLogResponseDto reportIssue(RepairLogCreateDto createDto, String username);

    List<RepairLogResponseDto> getRepairHistoryForAsset(Long assetId);

    RepairLogResponseDto updateRepairStatus(Long repairId, RepairLogUpdateDto updateDto);
}
