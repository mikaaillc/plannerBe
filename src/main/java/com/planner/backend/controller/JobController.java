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

        if ("FREE_ENTITY".equals(creator.getSubscriptionType())) {
            long jobCount = jobRepository.countByCreatorId(creator.getId());
            if (jobCount >= 2) {
                return ResponseEntity.badRequest().body("İş oluşturma limitinize ulaştınız (Maksimum 2). Lütfen PRO pakete geçin.");
            }
        }

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .jobType(request.getJobType())
                .minKarne(request.getMinKarne())
                .priceRangeMin(request.getPriceRangeMin())
                .priceRangeMax(request.getPriceRangeMax())
                .detailedInfo(request.getDetailedInfo())
                .isNazimImarPlani(request.getIsNazimImarPlani())
                .isUygulamaImarPlani(request.getIsUygulamaImarPlani())
                .hasZeminEtudu(request.getHasZeminEtudu())
                .hasHalihazirHarita(request.getHasHalihazirHarita())
                .hasKurumGorusleri(request.getHasKurumGorusleri())
                .isParselasyon(request.getIsParselasyon())
                .areaSize(request.getAreaSize())
                .locationDetails(request.getLocationDetails())
                .status("OPEN")
                .creator(creator)
                .build();

        return ResponseEntity.ok(jobRepository.save(job));
    }

    @GetMapping
    public List<Job> getAvailableJobs(@RequestParam(required = false) Long userId) {
        List<Job> jobs = jobRepository.findByStatusOrderByCreatedAtDesc("OPEN");
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && "FREE_PLANNER".equals(user.getSubscriptionType())) {
                // Mask creator names
                for (Job job : jobs) {
                    if (job.getCreator() != null) {
                        User creator = job.getCreator();
                        String fullName = creator.getFullName();
                        if (fullName != null && fullName.length() > 2) {
                            String masked = fullName.substring(0, 1) + "*** " + fullName.substring(fullName.lastIndexOf(" ") + 1 > 1 ? fullName.lastIndexOf(" ") + 1 : 1).substring(0, 1) + "***";
                            creator.setFullName(masked);
                        }
                    }
                }
            }
        }
        return jobs;
    }

    @GetMapping("/my-jobs/{creatorId}")
    public List<Job> getMyJobs(@PathVariable Long creatorId) {
        return jobRepository.findByCreatorIdOrderByCreatedAtDesc(creatorId);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job != null) {
            if (userId != null) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null && "FREE_PLANNER".equals(user.getSubscriptionType())) {
                    if (job.getCreator() != null) {
                        User creator = job.getCreator();
                        String fullName = creator.getFullName();
                        if (fullName != null && fullName.length() > 2) {
                            String masked = fullName.substring(0, 1) + "*** " + fullName.substring(fullName.lastIndexOf(" ") + 1 > 1 ? fullName.lastIndexOf(" ") + 1 : 1).substring(0, 1) + "***";
                            creator.setFullName(masked);
                        }
                    }
                }
            }
            return ResponseEntity.ok(job);
        }
        return ResponseEntity.notFound().build();
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
    private Boolean isNazimImarPlani;
    private Boolean isUygulamaImarPlani;
    private Boolean hasZeminEtudu;
    private Boolean hasHalihazirHarita;
    private Boolean hasKurumGorusleri;
    private Boolean isParselasyon;
    private Double areaSize;
    private String locationDetails;
}
