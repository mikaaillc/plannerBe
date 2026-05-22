package com.planner.backend.repository;

import com.planner.backend.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findBySenderId(Long senderId);
    List<Offer> findByJobId(Long jobId);
    List<Offer> findBySenderIdAndStatus(Long senderId, String status);
}
