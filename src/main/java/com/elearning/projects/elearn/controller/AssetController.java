package com.elearning.projects.elearn.controller;

import com.elearning.projects.elearn.dto.request.AssetCreateRequestDto;
import com.elearning.projects.elearn.dto.response.AssetResponseDto;
import com.elearning.projects.elearn.entity.enums.AssetStatus;
import com.elearning.projects.elearn.entity.enums.AssetType;
import com.elearning.projects.elearn.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    /**
     * Creates a new IT asset.
     * Only accessible by users with the ADMIN role.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody AssetCreateRequestDto assetDto) {
        AssetResponseDto createdAsset = assetService.createAsset(assetDto);
        return new ResponseEntity<>(createdAsset, HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated and filterable list of all IT assets.
     * Accessible by any authenticated user (ADMIN or EMPLOYEE).
     */
    @GetMapping
    public ResponseEntity<Page<AssetResponseDto>> getAllAssets(
            @RequestParam(required = false) AssetType type,
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<AssetResponseDto> assets = assetService.getAllAssets(pageable, type, status, employeeName, startDate, endDate);
        return ResponseEntity.ok(assets);
    }

    /**
     * Retrieves a single IT asset by its ID.
     * Accessible by any authenticated user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDto> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    /**
     * Updates an existing IT asset.
     * Only accessible by users with the ADMIN role.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetResponseDto> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetCreateRequestDto assetDetails) {
        return ResponseEntity.ok(assetService.updateAsset(id, assetDetails));
    }

    /**
     * Deletes an IT asset.
     * Only accessible by users with the ADMIN role.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
