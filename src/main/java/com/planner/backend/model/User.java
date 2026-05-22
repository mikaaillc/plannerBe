package com.planner.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String role; // "ROLE_PLANNER" or "ROLE_ENTITY"
    
    @Column
    private String entityType; // "KAMU" or "TUZEL" (if role is ROLE_ENTITY)
    
    @Column
    private String karne; // "A", "B", "C", "D", "E", "F" (if role is ROLE_PLANNER)

    @Column(columnDefinition = "TEXT")
    private String completedWorks; // Plancıların yaptığı işler

    @Column(columnDefinition = "TEXT")
    private String bio; // Kısa tanıtım / özgeçmiş

    @Column(columnDefinition = "TEXT")
    private String skills; // Uzmanlık alanları (virgülle ayrılmış)

    @Column
    private String location; // Şehir / Konum

    @Column
    private String phone; // İletişim numarası

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isPaid = false;

    @Column
    private String subscriptionType; // "FREE_ENTITY", "PRO_ENTITY", "FREE_PLANNER", "PRO_PLANNER", "PREMIUM_PLANNER"

    @Column
    private java.time.LocalDateTime subscriptionExpiryDate;
}
