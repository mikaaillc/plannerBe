package com.planner.backend.controller;

import com.planner.backend.model.Job;
import com.planner.backend.model.User;
import com.planner.backend.repository.JobRepository;
import com.planner.backend.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody CreateJobRequest request) {
        User creator = userRepository.findById(request.getCreatorId()).orElse(null);
        if (creator == null || !"ROLE_ENTITY".equals(creator.getRole())) {
            return ResponseEntity.badRequest().body("Creator not found or is not an Entity");
        }

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .jobType(request.getJobType())
                .minKarne(request.getMinKarne())
                .priceRangeMin(request.getPriceRangeMin())
                .priceRangeMax(request.getPriceRangeMax())
                .detailedInfo(request.getDetailedInfo())
                .status("OPEN")
                .creator(creator)
                .build();

        return ResponseEntity.ok(jobRepository.save(job));
    }

    @GetMapping
    public List<Job> getAvailableJobs() {
        return jobRepository.findByStatusOrderByCreatedAtDesc("OPEN");
    }

    @GetMapping("/my-jobs/{creatorId}")
    public List<Job> getMyJobs(@PathVariable Long creatorId) {
        return jobRepository.findByCreatorIdOrderByCreatedAtDesc(creatorId);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

@Data
class CreateJobRequest {
    private Long creatorId;
    private String title;
    private String description;
    private String jobType;
    private String minKarne;
    private Double priceRangeMin;
    private Double priceRangeMax;
    private String detailedInfo;
}
