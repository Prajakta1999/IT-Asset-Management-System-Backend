package com.elearning.projects.elearn.entity;

import com.elearning.projects.elearn.entity.enums.IssueType;
import com.elearning.projects.elearn.entity.enums.RepairStatus;
import com.elearning.projects.elearn.entity.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "repair_logs")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class RepairLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    // This now correctly links to your User entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_user_id", nullable = false)
    private User reportedBy;

    @Enumerated(EnumType.STRING)
    private IssueType issueType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private UrgencyLevel urgency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepairStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime reportedAt;

    private LocalDateTime resolvedAt;
}
