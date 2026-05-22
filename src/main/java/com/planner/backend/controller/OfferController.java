package com.planner.backend.controller;

import com.planner.backend.model.Comment;
import com.planner.backend.model.Job;
import com.planner.backend.model.Offer;
import com.planner.backend.model.User;
import com.planner.backend.repository.CommentRepository;
import com.planner.backend.repository.JobRepository;
import com.planner.backend.repository.OfferRepository;
import com.planner.backend.repository.UserRepository;
import lombok.Data;
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
    private JobRepository jobRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/job/{jobId}")
    public ResponseEntity<?> createOffer(@PathVariable Long jobId, @RequestBody OfferRequest request) {
        User sender = userRepository.findById(request.getSenderId()).orElse(null);
        Job job = jobRepository.findById(jobId).orElse(null);

        if (sender == null || job == null) {
            return ResponseEntity.badRequest().body("Invalid sender or job ID");
        }

        if (!sender.isPaid()) {
            return ResponseEntity.badRequest().body("Ödeme yapmayan plancılar teklif veremez.");
        }

        Offer offer = Offer.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .proposedPrice(request.getProposedPrice())
                .partnerKarnes(request.getPartnerKarnes())
                .status("PENDING")
                .sender(sender)
                .job(job)
                .build();

        offerRepository.save(offer);
        return ResponseEntity.ok(offer);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Offer>> getJobOffers(@PathVariable Long jobId) {
        return ResponseEntity.ok(offerRepository.findByJobId(jobId));
    }
    
    @GetMapping("/my-accepted-offers/{plannerId}")
    public ResponseEntity<List<Offer>> getAcceptedOffers(@PathVariable Long plannerId) {
        return ResponseEntity.ok(offerRepository.findBySenderIdAndStatus(plannerId, "ACCEPTED"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOffer(@PathVariable Long id) {
        return offerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptOffer(@PathVariable Long id) {
        Offer offer = offerRepository.findById(id).orElse(null);
        if (offer == null) return ResponseEntity.notFound().build();

        offer.setStatus("ACCEPTED");
        offerRepository.save(offer);
        
        Job job = offer.getJob();
        job.setStatus("IN_PROGRESS");
        jobRepository.save(job);
        
        // Note: Could reject other offers for the same job here.

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

@Data
class OfferRequest {
    private String title;
    private String description;
    private Double proposedPrice;
    private Long senderId;
    private String partnerKarnes;
}

@Data
class CommentRequest {
    private Long userId;
    private String text;
}
