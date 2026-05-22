package com.planner.backend.controller;

import com.planner.backend.model.User;
import com.planner.backend.repository.JobRepository;
import com.planner.backend.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/limits/{userId}")
    public ResponseEntity<?> getLimits(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if ("ROLE_ENTITY".equals(user.getRole())) {
            long jobCount = jobRepository.countByCreatorId(userId);
            long remaining = "FREE_ENTITY".equals(user.getSubscriptionType()) ? Math.max(0, 2 - jobCount) : -1; // -1 means unlimited
            return ResponseEntity.ok(new LimitResponse(jobCount, remaining));
        }

        return ResponseEntity.ok(new LimitResponse(0, -1)); // Planners don't have job creation limits
    }

    @PostMapping("/upgrade")
    public ResponseEntity<?> upgradeSubscription(@RequestBody UpgradeRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setSubscriptionType(request.getNewPlan());
        user.setPaid(!"FREE_ENTITY".equals(request.getNewPlan()) && !"FREE_PLANNER".equals(request.getNewPlan()));
        if (user.isPaid()) {
            user.setSubscriptionExpiryDate(LocalDateTime.now().plusYears(1)); // All paid plans are 1 year for now
        } else {
            user.setSubscriptionExpiryDate(null);
        }

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/cancel/{userId}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if ("ROLE_ENTITY".equals(user.getRole())) {
            user.setSubscriptionType("FREE_ENTITY");
        } else {
            user.setSubscriptionType("FREE_PLANNER");
        }
        user.setPaid(false);
        user.setSubscriptionExpiryDate(null);

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}

@Data
class LimitResponse {
    private long usedJobs;
    private long remainingJobs;

    public LimitResponse(long usedJobs, long remainingJobs) {
        this.usedJobs = usedJobs;
        this.remainingJobs = remainingJobs;
    }
}

@Data
class UpgradeRequest {
    private Long userId;
    private String newPlan;
}
