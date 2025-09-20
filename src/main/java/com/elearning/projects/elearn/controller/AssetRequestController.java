package com.elearning.projects.elearn.controller;

import com.elearning.projects.elearn.dto.request.AssetRequestCreateDto;
import com.elearning.projects.elearn.dto.request.AssetRequestReviewDto;
import com.elearning.projects.elearn.dto.response.AssetRequestResponseDto;
import com.elearning.projects.elearn.service.AssetRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/asset-requests")
@RequiredArgsConstructor
public class AssetRequestController {

    private final AssetRequestService assetRequestService;

    /**
     * Submits a new request for an IT asset.
     * Only accessible by users with the EMPLOYEE role.
     */
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AssetRequestResponseDto> createAssetRequest(@Valid @RequestBody AssetRequestCreateDto requestDto, Principal principal) {
        AssetRequestResponseDto createdRequest = assetRequestService.createAssetRequest(requestDto, principal.getName());
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    /**
     * Retrieves all requests made by the currently logged-in employee.
     * Only accessible by users with the EMPLOYEE role.
     */
    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<AssetRequestResponseDto>> getMyAssetRequests(Principal principal) {
        return ResponseEntity.ok(assetRequestService.getMyAssetRequests(principal.getName()));
    }

    /**
     * Retrieves all requests that are currently pending review.
     * Only accessible by users with the ADMIN role.
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AssetRequestResponseDto>> getAllPendingRequests() {
        return ResponseEntity.ok(assetRequestService.getAllPendingRequests());
    }

    /**
     * Approves or rejects an asset request.
     * Only accessible by users with the ADMIN role.
     */
    @PutMapping("/{requestId}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetRequestResponseDto> reviewAssetRequest(@PathVariable Long requestId, @Valid @RequestBody AssetRequestReviewDto reviewDto, Principal principal) {
        return ResponseEntity.ok(assetRequestService.reviewAssetRequest(requestId, reviewDto, principal.getName()));
    }
}
