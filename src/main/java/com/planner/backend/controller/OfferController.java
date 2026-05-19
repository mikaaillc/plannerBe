package com.planner.backend.controller;

import com.planner.backend.model.Comment;
import com.planner.backend.model.Offer;
import com.planner.backend.model.User;
import com.planner.backend.repository.CommentRepository;
import com.planner.backend.repository.OfferRepository;
import com.planner.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping
    public ResponseEntity<?> createOffer(@RequestBody OfferRequest request) {
        User sender = userRepository.findById(request.getSenderId()).orElse(null);
        User receiver = userRepository.findById(request.getReceiverId()).orElse(null);

        if (sender == null || receiver == null) {
            return ResponseEntity.badRequest().body("Invalid sender or receiver ID");
        }

        if (!sender.isPaid()) {
            return ResponseEntity.badRequest().body("Ödeme yapmayan kullanıcılar teklif veremez.");
        }

        if ("ROLE_ENTITY".equals(receiver.getRole()) && !receiver.isPaid()) {
            return ResponseEntity.badRequest().body("Ödeme yapmayan tüzel kişiler teklif alamaz.");
        }

        Offer offer = Offer.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .proposedPrice(request.getProposedPrice())
                .status("PENDING")
                .sender(sender)
                .receiver(receiver)
                .build();

        offerRepository.save(offer);
        return ResponseEntity.ok(offer);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Offer>> getUserOffers(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if ("ROLE_PLANNER".equals(user.getRole())) {
            return ResponseEntity.ok(offerRepository.findByReceiverId(userId));
        } else {
            return ResponseEntity.ok(offerRepository.findBySenderId(userId));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOffer(@PathVariable Long id) {
        return offerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        Offer offer = offerRepository.findById(id).orElse(null);
        if (offer == null) return ResponseEntity.notFound().build();

        offer.setStatus(request.getStatus());
        if (request.getProposedPrice() != null) {
            offer.setProposedPrice(request.getProposedPrice());
        }
        offerRepository.save(offer);
        return ResponseEntity.ok(offer);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(commentRepository.findByOfferId(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        Offer offer = offerRepository.findById(id).orElse(null);
        User user = userRepository.findById(request.getUserId()).orElse(null);

        if (offer == null || user == null) {
            return ResponseEntity.badRequest().body("Invalid offer or user");
        }

        Comment comment = Comment.builder()
                .text(request.getText())
                .offer(offer)
                .user(user)
                .build();
        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }
}

class OfferRequest {
    private String title;
    private String description;
    private Double proposedPrice;
    private Long senderId;
    private Long receiverId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getProposedPrice() { return proposedPrice; }
    public void setProposedPrice(Double proposedPrice) { this.proposedPrice = proposedPrice; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
}

class StatusUpdateRequest {
    private String status;
    private Double proposedPrice;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getProposedPrice() { return proposedPrice; }
    public void setProposedPrice(Double proposedPrice) { this.proposedPrice = proposedPrice; }
}

class CommentRequest {
    private Long userId;
    private String text;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
