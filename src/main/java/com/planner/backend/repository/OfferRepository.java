package com.planner.backend.repository;

import com.planner.backend.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findBySenderId(Long senderId);
    List<Offer> findByReceiverId(Long receiverId);
}
