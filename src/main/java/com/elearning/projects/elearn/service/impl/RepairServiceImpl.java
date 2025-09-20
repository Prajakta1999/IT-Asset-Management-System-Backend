package com.elearning.projects.elearn.service.impl;

import com.elearning.projects.elearn.dto.request.RepairLogCreateDto;
import com.elearning.projects.elearn.dto.request.RepairLogUpdateDto;
import com.elearning.projects.elearn.dto.response.RepairLogResponseDto;
import com.elearning.projects.elearn.entity.Asset;
import com.elearning.projects.elearn.entity.RepairLog;
import com.elearning.projects.elearn.entity.User;
import com.elearning.projects.elearn.entity.enums.AssetStatus;
import com.elearning.projects.elearn.entity.enums.RepairStatus;
import com.elearning.projects.elearn.exception.BadRequestException;
import com.elearning.projects.elearn.exception.ResourceNotFoundException;
import com.elearning.projects.elearn.repository.AssetRepository;
import com.elearning.projects.elearn.repository.RepairLogRepository;
import com.elearning.projects.elearn.repository.UserRepository;
import com.elearning.projects.elearn.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepairServiceImpl implements RepairService {

    private final RepairLogRepository repairLogRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    // The parameter order has been corrected to match the RepairService interface
    public RepairLogResponseDto reportIssue(RepairLogCreateDto repairDto, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));

        Asset asset = assetRepository.findById(repairDto.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + repairDto.assetId()));

        if (asset.getAssignedTo() == null || !asset.getAssignedTo().equals(user)) {
            throw new BadRequestException("You can only report issues for assets assigned to you.");
        }

        // When an issue is reported, the asset is now "IN_REPAIR"
        asset.setStatus(AssetStatus.IN_REPAIR);
        assetRepository.save(asset);

        RepairLog repairLog = new RepairLog();
        repairLog.setAsset(asset);
        repairLog.setReportedBy(user);
        repairLog.setIssueType(repairDto.issueType());
        repairLog.setDescription(repairDto.description());
        repairLog.setUrgency(repairDto.urgency());
        repairLog.setStatus(RepairStatus.REPORTED); // Initial status
        repairLog.setReportedAt(LocalDateTime.now());

        RepairLog savedLog = repairLogRepository.save(repairLog);
        return DtoMapper.toRepairLogResponseDto(savedLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepairLogResponseDto> getRepairHistoryForAsset(Long assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new ResourceNotFoundException("Asset not found with id: " + assetId);
        }
        List<RepairLog> history = repairLogRepository.findByAssetIdOrderByReportedAtDesc(assetId);
        return history.stream()
                .map(DtoMapper::toRepairLogResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RepairLogResponseDto updateRepairStatus(Long repairId, RepairLogUpdateDto updateDto) {
        RepairLog repairLog = repairLogRepository.findById(repairId)
                .orElseThrow(() -> new ResourceNotFoundException("Repair log not found with id: " + repairId));

        repairLog.setStatus(updateDto.status());

        // If the repair is completed or not repairable, update the asset status accordingly
        if (updateDto.status() == RepairStatus.REPAIRED) {
            repairLog.setResolvedAt(LocalDateTime.now());
            Asset asset = repairLog.getAsset();
            asset.setStatus(AssetStatus.AVAILABLE); // It's now available for reassignment
            assetRepository.save(asset);
        } else if (updateDto.status() == RepairStatus.NOT_REPAIRABLE) {
            repairLog.setResolvedAt(LocalDateTime.now());
            Asset asset = repairLog.getAsset();
            asset.setStatus(AssetStatus.RETIRED);
            assetRepository.save(asset);
        }

        RepairLog updatedLog = repairLogRepository.save(repairLog);
        return DtoMapper.toRepairLogResponseDto(updatedLog);
    }
}

