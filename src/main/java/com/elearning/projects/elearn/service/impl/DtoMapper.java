package com.elearning.projects.elearn.service.impl;

import com.elearning.projects.elearn.dto.response.*;
import com.elearning.projects.elearn.entity.Asset;
import com.elearning.projects.elearn.entity.AssetRequest;
import com.elearning.projects.elearn.entity.RepairLog;
import com.elearning.projects.elearn.entity.User;

// A helper class to centralize mapping logic
public class DtoMapper {

    public static UserSummaryDto toUserSummaryDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserSummaryDto(user.getId(), user.getName(), user.getEmail());
    }

    public static AssetResponseDto toAssetResponseDto(Asset asset) {
        if (asset == null) {
            return null;
        }
        return new AssetResponseDto(
                asset.getId(),
                asset.getAssetType(),
                asset.getManufacturer(),
                asset.getModel(),
                asset.getSerialNumber(),
                asset.getPurchaseDate(),
                asset.getWarrantyExpiry(),
                asset.getStatus(),
                toUserSummaryDto(asset.getAssignedTo())
        );
    }

    public static AssetRequestResponseDto toAssetRequestResponseDto(AssetRequest request) {
        if (request == null) {
            return null;
        }
        return new AssetRequestResponseDto(
                request.getId(),
                request.getAssetType(),
                request.getJustification(),
                request.getPreferredSpecifications(),
                request.getStatus(),
                request.getRejectionReason(),
                toUserSummaryDto(request.getRequestedBy()),
                toUserSummaryDto(request.getReviewedBy()),
                toAssetResponseDto(request.getAssignedAsset()),
                request.getCreatedAt()
        );
    }
    
    public static RepairLogResponseDto toRepairLogResponseDto(RepairLog log) {
        if(log == null) {
            return null;
        }
        return new RepairLogResponseDto(
                log.getId(),
                log.getAsset().getId(),
                log.getAsset().getSerialNumber(),
                toUserSummaryDto(log.getReportedBy()),
                log.getIssueType(),
                log.getDescription(),
                log.getUrgency(),
                log.getStatus(),
                log.getReportedAt(),
                log.getResolvedAt()
        );
    }
}
