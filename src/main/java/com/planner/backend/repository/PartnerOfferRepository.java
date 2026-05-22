package com.planner.backend.repository;

import com.planner.backend.model.PartnerOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerOfferRepository extends JpaRepository<PartnerOffer, Long> {
    List<PartnerOffer> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    List<PartnerOffer> findBySenderIdOrderByCreatedAtDesc(Long senderId);
}
