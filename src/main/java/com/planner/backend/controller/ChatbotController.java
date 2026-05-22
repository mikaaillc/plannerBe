package com.planner.backend.controller;

import com.planner.backend.model.ErrorReport;
import com.planner.backend.model.User;
import com.planner.backend.repository.ErrorReportRepository;
import com.planner.backend.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    @Value("${gemini.api.key:YOUR_API_KEY}")
    private String geminiApiKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ErrorReportRepository errorReportRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
        if (geminiApiKey == null || geminiApiKey.equals("YOUR_API_KEY") || geminiApiKey.isEmpty()) {
            return ResponseEntity.status(500).body(Map.of("response", "API key not configured. Please configure gemini.api.key"));
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.0-pro:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "Sen bir şehir plancısı asistanısın. Kullanıcıya yardımcı ol. Kullanıcı sorusu: " + request.getMessage();

        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt.replace("\"", "\\\"") + "\"}]}]}";
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (!parts.isEmpty()) {
                        String text = (String) parts.get(0).get("text");
                        return ResponseEntity.ok(Map.of("response", text));
                    }
                }
            }
            return ResponseEntity.ok(Map.of("response", "No response from Gemini"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("response", "Error communicating with Gemini: " + e.getMessage()));
        }
    }

    @PostMapping("/report-error")
    public ResponseEntity<?> reportError(@RequestBody ErrorReportRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid user");
        }

        ErrorReport report = ErrorReport.builder()
                .message(request.getMessage())
                .reportedBy(user)
                .build();
        errorReportRepository.save(report);

        return ResponseEntity.ok(Map.of("message", "Error reported successfully"));
    }
}

@Data
class ChatRequest {
    private String message;
    private Long userId; // optional, for context
}

@Data
class ErrorReportRequest {
    private String message;
    private Long userId;
}
