package com.elearning.projects.elearn.service.impl;

import com.elearning.projects.elearn.dto.request.AssetRequestCreateDto;
import com.elearning.projects.elearn.dto.request.AssetRequestReviewDto;
import com.elearning.projects.elearn.dto.response.AssetRequestResponseDto;
import com.elearning.projects.elearn.entity.Asset;
import com.elearning.projects.elearn.entity.AssetRequest;
import com.elearning.projects.elearn.entity.User;
import com.elearning.projects.elearn.entity.enums.AssetRequestStatus;
import com.elearning.projects.elearn.entity.enums.AssetStatus;
import com.elearning.projects.elearn.exception.BadRequestException;
import com.elearning.projects.elearn.exception.ResourceNotFoundException;
import com.elearning.projects.elearn.repository.AssetRepository;
import com.elearning.projects.elearn.repository.AssetRequestRepository;
import com.elearning.projects.elearn.repository.UserRepository;
import com.elearning.projects.elearn.service.AssetRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetRequestServiceImpl implements AssetRequestService {

    private final AssetRequestRepository assetRequestRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    @Override
    @Transactional
    public AssetRequestResponseDto createAssetRequest(AssetRequestCreateDto requestDto, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));

        AssetRequest newRequest = new AssetRequest();
        newRequest.setRequestedBy(user);
        newRequest.setAssetType(requestDto.assetType());
        newRequest.setJustification(requestDto.justification());
        newRequest.setPreferredSpecifications(requestDto.preferredSpecifications());
        newRequest.setStatus(AssetRequestStatus.PENDING); // New requests are always pending

        AssetRequest savedRequest = assetRequestRepository.save(newRequest);
        return DtoMapper.toAssetRequestResponseDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetRequestResponseDto> getMyAssetRequests(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));

        return assetRequestRepository.findByRequestedBy(user).stream()
                .map(DtoMapper::toAssetRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetRequestResponseDto> getAllPendingRequests() {
        return assetRequestRepository.findByStatus(AssetRequestStatus.PENDING).stream()
                .map(DtoMapper::toAssetRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssetRequestResponseDto reviewAssetRequest(Long requestId, AssetRequestReviewDto reviewDto, String adminUsername) {
        User admin = userRepository.findByEmail(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with email: " + adminUsername));

        AssetRequest request = assetRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset request not found with id: " + requestId));

        if (request.getStatus() != AssetRequestStatus.PENDING) {
            throw new BadRequestException("This request has already been reviewed.");
        }

        request.setReviewedBy(admin);

        if (reviewDto.status() == AssetRequestStatus.APPROVED) {
            if (reviewDto.assetIdToAssign() == null) {
                throw new BadRequestException("An asset ID must be provided to approve a request.");
            }
            Asset assetToAssign = assetRepository.findById(reviewDto.assetIdToAssign())
                    .orElseThrow(() -> new ResourceNotFoundException("Asset to assign not found with id: " + reviewDto.assetIdToAssign()));

            if (assetToAssign.getStatus() != AssetStatus.AVAILABLE) {
                throw new BadRequestException("The selected asset is not available for assignment.");
            }
            
            // Core Logic: Approve request, assign asset to user, and update asset status
            assetToAssign.setAssignedTo(request.getRequestedBy());
            assetToAssign.setStatus(AssetStatus.ASSIGNED);
            assetRepository.save(assetToAssign);

            request.setAssignedAsset(assetToAssign);
            request.setStatus(AssetRequestStatus.APPROVED);

        } else if (reviewDto.status() == AssetRequestStatus.REJECTED) {
            if (reviewDto.rejectionReason() == null || reviewDto.rejectionReason().isBlank()) {
                throw new BadRequestException("A reason must be provided to reject a request.");
            }
            request.setStatus(AssetRequestStatus.REJECTED);
            request.setRejectionReason(reviewDto.rejectionReason());
        } else {
            throw new BadRequestException("Invalid review status. Must be APPROVED or REJECTED.");
        }

        AssetRequest updatedRequest = assetRequestRepository.save(request);
        return DtoMapper.toAssetRequestResponseDto(updatedRequest);
    }
}
