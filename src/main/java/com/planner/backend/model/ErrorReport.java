package com.planner.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "error_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User reportedBy;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
