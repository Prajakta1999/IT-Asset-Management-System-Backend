package com.elearning.projects.elearn.controller;

import com.elearning.projects.elearn.dto.request.RepairLogCreateDto;
import com.elearning.projects.elearn.dto.request.RepairLogUpdateDto;
import com.elearning.projects.elearn.dto.response.RepairLogResponseDto;
import com.elearning.projects.elearn.service.RepairService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/repairs")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;

    /**
     * Reports a new issue for an assigned asset.
     * Only accessible by users with the EMPLOYEE role.
     */
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<RepairLogResponseDto> reportIssue(@Valid @RequestBody RepairLogCreateDto createDto, Principal principal) {
        RepairLogResponseDto createdLog = repairService.reportIssue(createDto, principal.getName());
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    /**
     * Retrieves the full repair history for a specific asset.
     * Accessible by any authenticated user.
     */
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<RepairLogResponseDto>> getRepairHistoryForAsset(@PathVariable Long assetId) {
        return ResponseEntity.ok(repairService.getRepairHistoryForAsset(assetId));
    }

    /**
     * Updates the status of a repair log entry.
     * Only accessible by users with the ADMIN role.
     */
    @PutMapping("/{repairId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RepairLogResponseDto> updateRepairStatus(@PathVariable Long repairId, @Valid @RequestBody RepairLogUpdateDto updateDto) {
        return ResponseEntity.ok(repairService.updateRepairStatus(repairId, updateDto));
    }
}
