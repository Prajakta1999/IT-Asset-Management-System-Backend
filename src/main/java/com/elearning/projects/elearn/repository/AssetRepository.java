package com.elearning.projects.elearn.repository;

import com.elearning.projects.elearn.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {
    /**
     * Checks if an asset with the given serial number already exists.
     * This is useful for validation when creating a new asset.
     * @param serialNumber The serial number to check.
     * @return true if an asset with this serial number exists, false otherwise.
     */
    boolean existsBySerialNumber(String serialNumber);

    /**
     * Finds an asset by its unique serial number.
     * @param serialNumber The serial number of the asset.
     * @return An Optional containing the Asset if found.
     */
    Optional<Asset> findBySerialNumber(String serialNumber);
}
