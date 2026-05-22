package com.planner.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double proposedPrice;

    private String status; // PENDING, ACCEPTED, REJECTED, NEGOTIATING

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // The planner making the offer

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job; // The job this offer belongs to
    
    @Column
    private String partnerKarnes; // Comma-separated list of partner karnes, e.g. "A,B"

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
