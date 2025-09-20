package com.elearning.projects.elearn.repository;

import com.elearning.projects.elearn.entity.AssetRequest;
import com.elearning.projects.elearn.entity.User;
import com.elearning.projects.elearn.entity.enums.AssetRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRequestRepository extends JpaRepository<AssetRequest, Long> {
    /**
     * Finds all asset requests with a specific status.
     * @param status The status to filter by (e.g., PENDING).
     * @return A list of matching asset requests.
     */
    List<AssetRequest> findByStatus(AssetRequestStatus status);

    /**
     * Finds all asset requests made by a specific user.
     * @param user The employee who made the requests.
     * @return A list of the user's asset requests.
     */
    List<AssetRequest> findByRequestedBy(User user);
}
