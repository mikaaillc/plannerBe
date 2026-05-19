package com.planner.backend.controller;

import com.planner.backend.model.ErrorReport;
import com.planner.backend.model.User;
import com.planner.backend.repository.ErrorReportRepository;
import com.planner.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ErrorReportRepository errorReportRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/error-reports")
    public ResponseEntity<List<ErrorReport>> getErrorReports() {
        return ResponseEntity.ok(errorReportRepository.findAll());
    }
}
