package com.tutedude.proctoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.tutedude.proctoring") // Explicit component scanning
@RestController
public class VideoProctoringBackendApplication {

    public static void main(String[] args) {
        System.out.println("Starting Video Proctoring System...");
        System.out.println("Package: com.tutedude.proctoring");
        System.out.println("Timestamp: " + LocalDateTime.now());
        
        SpringApplication.run(VideoProctoringBackendApplication.class, args);
        
        System.out.println("Video Proctoring System started successfully!");
        System.out.println("Backend API available at: http://localhost:8080/api/proctoring");
        System.out.println("Health check available at: http://localhost:8080/api/proctoring/health");
        System.out.println("Test endpoint: http://localhost:8080/test");
    }

    // Root endpoint
    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Video Proctoring System API");
        response.put("version", "1.0.0");
        response.put("status", "Running");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Welcome to Video Proctoring API");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/api/proctoring/health");
        endpoints.put("start_session", "POST /api/proctoring/sessions/start");
        endpoints.put("end_session", "POST /api/proctoring/sessions/{sessionId}/end");
        endpoints.put("add_event", "POST /api/proctoring/sessions/{sessionId}/events");
        endpoints.put("get_report", "GET /api/proctoring/sessions/{sessionId}/report");
        endpoints.put("test", "GET /test");
        
        response.put("endpoints", endpoints);
        
        System.out.println("📋 Root endpoint accessed at: " + LocalDateTime.now());
        return response;
    }
    
    // Simple test endpoint
    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Spring Boot is working correctly!");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SUCCESS");
        response.put("controller_scan", "Controllers should be detected now");
        
        System.out.println("🧪 Test endpoint accessed successfully!");
        return response;
    }
}