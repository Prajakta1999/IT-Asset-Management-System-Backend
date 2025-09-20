package com.elearning.projects.elearn.repository;

import com.elearning.projects.elearn.entity.RepairLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairLogRepository extends JpaRepository<RepairLog, Long> {

    // Add this method to the interface
    // Spring Data JPA will automatically create the query based on the method name:
    // "Find all RepairLog entities by the asset's ID, and order them by the reportedAt field in descending order."
    List<RepairLog> findByAssetIdOrderByReportedAtDesc(Long assetId);
}

