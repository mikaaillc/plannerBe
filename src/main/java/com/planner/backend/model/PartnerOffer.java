package com.planner.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partner_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    private Double proposedFee;

    @Column
    private String status; // PENDING, ACCEPTED, REJECTED

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
