package com.elearning.projects.elearn.entity;

import com.elearning.projects.elearn.entity.enums.AssetRequestStatus;
import com.elearning.projects.elearn.entity.enums.AssetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "asset_requests")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AssetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This now correctly links to your User entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_user_id", nullable = false)
    private User requestedBy;
    
    // This now correctly links to your User entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_user_id")
    private User reviewedBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_asset_id")
    private Asset assignedAsset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType assetType;

    @Column(columnDefinition = "TEXT")
    private String justification;

    private String preferredSpecifications;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetRequestStatus status;
    
    private String rejectionReason;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;
}
