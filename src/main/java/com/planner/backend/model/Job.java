package com.planner.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private String jobType; // "DANIŞMANLIK" or "PLANLAMA"
    
    @Column
    private String minKarne; // "A", "B", "C", "D", "E", "F"
    
    @Column
    private Double priceRangeMin;
    
    @Column
    private Double priceRangeMax;
    
    @Column(columnDefinition = "TEXT")
    private String detailedInfo; // Ada/Parsel vs.

    // --- Yeni Eklenen Planlama Detay Alanları ---
    @Column
    private Boolean isNazimImarPlani;

    @Column
    private Boolean isUygulamaImarPlani;

    @Column
    private Boolean isParselasyon;

    @Column
    private Boolean hasZeminEtudu;

    @Column
    private Boolean hasHalihazirHarita;

    @Column
    private Boolean hasKurumGorusleri;

    @Column
    private Double areaSize; // Hektar

    @Column
    private String locationDetails; // İl/İlçe/Mahalle
    // ------------------------------------------

    @Column
    private String status; // "OPEN", "IN_PROGRESS", "COMPLETED", "CANCELLED"

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator; // The entity (Tüzel or Kamu) who created the job

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
