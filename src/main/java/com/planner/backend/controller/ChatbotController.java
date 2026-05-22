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

    @Value("${groq.api.key:YOUR_API_KEY}")
    private String groqApiKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ErrorReportRepository errorReportRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
        if (groqApiKey == null || groqApiKey.equals("YOUR_API_KEY") || groqApiKey.isEmpty()) {
            return ResponseEntity.status(500).body(Map.of("response", "API key not configured. Please configure groq.api.key in application.properties or GROQ_API_KEY in environment variables"));
        }

        String url = "https://api.groq.com/openai/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        // Escape JSON properly
        String safeMessage = request.getMessage().replace("\"", "\\\"").replace("\n", "\\n");
        String requestBody = "{\n" +
                "  \"model\": \"llama3-8b-8192\",\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"role\": \"system\",\n" +
                "      \"content\": \"Sen bir şehir plancısı asistanısın. Kullanıcıya profesyonelce yardımcı ol.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"content\": \"" + safeMessage + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                    if (messageObj != null && messageObj.containsKey("content")) {
                        String text = (String) messageObj.get("content");
                        return ResponseEntity.ok(Map.of("response", text));
                    }
                }
            }
            return ResponseEntity.ok(Map.of("response", "No response from Groq"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("response", "Error communicating with Groq: " + e.getMessage()));
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
