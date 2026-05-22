package com.planner.backend.controller;

import com.planner.backend.model.User;
import com.planner.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/planners")
public class PlannerController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/partners")
    public ResponseEntity<?> getPotentialPartners(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !"ROLE_PLANNER".equals(user.getRole())) {
            return ResponseEntity.badRequest().body("Sadece plancılar partner arayabilir.");
        }

        if (!"PREMIUM_PLANNER".equals(user.getSubscriptionType())) {
            return ResponseEntity.badRequest().body("Partner arama özelliği sadece PREMIUM plancılar içindir.");
        }

        List<User> allPlanners = userRepository.findAll().stream()
                .filter(u -> "ROLE_PLANNER".equals(u.getRole()))
                .filter(u -> !u.getId().equals(userId))
                .collect(Collectors.toList());

        // Karne sıralaması (A en yüksek, F en düşük)
        // Eğer kullanıcının karnesi C ise, sadece A ve B olanları getirebiliriz (veya filtreyi front-end'de yapabiliriz).
        // Front-end'in filtreleyebilmesi için tüm plancıları dönmek daha esnek olabilir. 
        // Şimdilik tüm plancıları dönüyoruz, front-end filtreleyecek.
        
        return ResponseEntity.ok(allPlanners);
    }
}
