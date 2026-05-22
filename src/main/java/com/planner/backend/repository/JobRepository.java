package com.planner.backend.repository;

import com.planner.backend.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);
    List<Job> findByStatusOrderByCreatedAtDesc(String status);
    long countByCreatorId(Long creatorId);
}
