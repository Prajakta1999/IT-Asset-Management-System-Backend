package com.elearning.projects.elearn.service.impl;

import com.elearning.projects.elearn.dto.request.AssetCreateRequestDto;
import com.elearning.projects.elearn.dto.response.AssetResponseDto;
import com.elearning.projects.elearn.entity.Asset;
import com.elearning.projects.elearn.entity.enums.AssetStatus;
import com.elearning.projects.elearn.entity.enums.AssetType;
import com.elearning.projects.elearn.exception.BadRequestException;
import com.elearning.projects.elearn.exception.ResourceNotFoundException;
import com.elearning.projects.elearn.repository.AssetRepository;
import com.elearning.projects.elearn.service.AssetService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    @Override
    @Transactional
    public AssetResponseDto createAsset(AssetCreateRequestDto assetDto) {
        // Validate that the serial number is unique before creating
        if (assetRepository.existsBySerialNumber(assetDto.serialNumber())) {
            throw new BadRequestException("Asset with serial number '" + assetDto.serialNumber() + "' already exists.");
        }

        Asset asset = new Asset();
        asset.setAssetType(assetDto.assetType());
        asset.setManufacturer(assetDto.manufacturer());
        asset.setModel(assetDto.model());
        asset.setSerialNumber(assetDto.serialNumber());
        asset.setPurchaseDate(assetDto.purchaseDate());
        asset.setWarrantyExpiry(assetDto.warrantyExpiry());
        asset.setStatus(AssetStatus.AVAILABLE); // New assets are always available

        Asset savedAsset = assetRepository.save(asset);
        return DtoMapper.toAssetResponseDto(savedAsset);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetResponseDto getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        return DtoMapper.toAssetResponseDto(asset);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetResponseDto> getAllAssets(Pageable pageable, AssetType type, AssetStatus status, String employeeName, LocalDate startDate, LocalDate endDate) {
        // Use JPA specifications for dynamic filtering based on provided criteria
        Specification<Asset> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("assetType"), type));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (employeeName != null && !employeeName.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("assignedTo").get("name")), "%" + employeeName.toLowerCase() + "%"));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("purchaseDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("purchaseDate"), endDate));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Asset> assetPage = assetRepository.findAll(spec, pageable);
        return assetPage.map(DtoMapper::toAssetResponseDto);
    }

    @Override
    @Transactional
    public AssetResponseDto updateAsset(Long id, AssetCreateRequestDto assetDetails) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        // Check for serial number uniqueness if it's being changed
        if (!asset.getSerialNumber().equals(assetDetails.serialNumber()) && assetRepository.existsBySerialNumber(assetDetails.serialNumber())) {
            throw new BadRequestException("Another asset with serial number '" + assetDetails.serialNumber() + "' already exists.");
        }

        asset.setAssetType(assetDetails.assetType());
        asset.setManufacturer(assetDetails.manufacturer());
        asset.setModel(assetDetails.model());
        asset.setSerialNumber(assetDetails.serialNumber());
        asset.setPurchaseDate(assetDetails.purchaseDate());
        asset.setWarrantyExpiry(assetDetails.warrantyExpiry());

        Asset updatedAsset = assetRepository.save(asset);
        return DtoMapper.toAssetResponseDto(updatedAsset);
    }

    @Override
    @Transactional
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        // Business Rule: Cannot delete an asset that is currently assigned to someone.
        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new BadRequestException("Cannot delete an asset that is currently assigned. Please reassign or retire it first.");
        }
        assetRepository.delete(asset);
    }
}
