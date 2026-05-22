package com.planner.backend.controller;

import com.planner.backend.model.Job;
import com.planner.backend.model.PartnerOffer;
import com.planner.backend.model.User;
import com.planner.backend.repository.JobRepository;
import com.planner.backend.repository.PartnerOfferRepository;
import com.planner.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partner-offers")
@CrossOrigin(origins = "http://localhost:4200")
public class PartnerOfferController {

    @Autowired
    private PartnerOfferRepository partnerOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @PostMapping
    public ResponseEntity<?> createOffer(@RequestBody Map<String, Object> payload) {
        try {
            Long senderId = Long.valueOf(payload.get("senderId").toString());
            Long receiverId = Long.valueOf(payload.get("receiverId").toString());
            Long jobId = Long.valueOf(payload.get("jobId").toString());
            Double proposedFee = payload.get("proposedFee") != null ? Double.valueOf(payload.get("proposedFee").toString()) : null;
            String message = (String) payload.get("message");

            User sender = userRepository.findById(senderId).orElse(null);
            User receiver = userRepository.findById(receiverId).orElse(null);
            Job job = jobRepository.findById(jobId).orElse(null);

            if (sender == null || receiver == null || job == null) {
                return ResponseEntity.badRequest().body("Gerekli bilgiler eksik veya hatalı.");
            }

            // Check if sender is PREMIUM
            if (!"PREMIUM_PLANNER".equals(sender.getSubscriptionType())) {
                return ResponseEntity.badRequest().body("Sadece Premium Plancılar teklif gönderebilir.");
            }
            
            // Check if receiver is PREMIUM
            if (!"PREMIUM_PLANNER".equals(receiver.getSubscriptionType())) {
                return ResponseEntity.badRequest().body("Karşı taraf Premium Plancı olmadığı için teklif alamaz.");
            }

            PartnerOffer offer = PartnerOffer.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .job(job)
                    .message(message)
                    .proposedFee(proposedFee)
                    .status("PENDING")
                    .build();

            return ResponseEntity.ok(partnerOfferRepository.save(offer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        }
    }

    @GetMapping("/received")
    public ResponseEntity<List<PartnerOffer>> getReceivedOffers(@RequestParam Long userId) {
        return ResponseEntity.ok(partnerOfferRepository.findByReceiverIdOrderByCreatedAtDesc(userId));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<PartnerOffer>> getSentOffers(@RequestParam Long userId) {
        return ResponseEntity.ok(partnerOfferRepository.findBySenderIdOrderByCreatedAtDesc(userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String newStatus = payload.get("status");
            PartnerOffer offer = partnerOfferRepository.findById(id).orElse(null);
            if (offer == null) {
                return ResponseEntity.notFound().build();
            }
            offer.setStatus(newStatus);
            return ResponseEntity.ok(partnerOfferRepository.save(offer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        }
    }
}
