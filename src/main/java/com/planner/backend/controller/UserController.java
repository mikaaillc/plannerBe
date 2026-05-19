package com.planner.backend.controller;

import com.planner.backend.model.User;
import com.planner.backend.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody UpdateProfileRequest request) {
        return userRepository.findById(id).map(user -> {
            if (request.getFullName() != null) user.setFullName(request.getFullName());
            if (request.getBio() != null) user.setBio(request.getBio());
            if (request.getSkills() != null) user.setSkills(request.getSkills());
            if (request.getCompletedWorks() != null) user.setCompletedWorks(request.getCompletedWorks());
            if (request.getLocation() != null) user.setLocation(request.getLocation());
            if (request.getPhone() != null) user.setPhone(request.getPhone());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Long id, @RequestBody SubscriptionRequest request) {
        return userRepository.findById(id).map(user -> {
            user.setPaid(true);
            user.setSubscriptionType(request.getType());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }
}

@Data
class UpdateProfileRequest {
    private String fullName;
    private String bio;
    private String skills;
    private String completedWorks;
    private String location;
    private String phone;
}

@Data
class SubscriptionRequest {
    private String type;
}
