package com.tutedude.proctoring.controller;

import com.tutedude.proctoring.model.DetectionEvent;
import com.tutedude.proctoring.model.InterviewSession;
import com.tutedude.proctoring.model.ProctoringReport;
import com.tutedude.proctoring.service.ProctoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proctoring")
@CrossOrigin(origins = "*")
public class ProctoringController {

	
	
    @Autowired
    private ProctoringService proctoringService;
    
    @PostMapping("/sessions/start")
    public ResponseEntity<Map<String, String>> startSession(@RequestBody Map<String, String> request) {
        String candidateName = request.get("candidateName");
        String sessionId = proctoringService.startSession(candidateName);
        return ResponseEntity.ok(Map.of("sessionId", sessionId, "status", "started"));
    }
    
    @PostMapping("/sessions/{sessionId}/end")
    public ResponseEntity<InterviewSession> endSession(@PathVariable String sessionId) {
        InterviewSession session = proctoringService.endSession(sessionId);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/sessions/{sessionId}/events")
    public ResponseEntity<String> addDetectionEvent(
            @PathVariable String sessionId,
            @RequestBody DetectionEvent event) {
        
        proctoringService.addDetectionEvent(
            sessionId, 
            event.getEventType(), 
            event.getMessage(), 
            event.getSeverity()
        );
        return ResponseEntity.ok("Event recorded");
    }
    
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<InterviewSession> getSession(@PathVariable String sessionId) {
        InterviewSession session = proctoringService.getSession(sessionId);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/sessions")
    public ResponseEntity<List<InterviewSession>> getAllSessions() {
        return ResponseEntity.ok(proctoringService.getAllSessions());
    }
    
    @GetMapping("/sessions/{sessionId}/report")
    public ResponseEntity<ProctoringReport> getReport(@PathVariable String sessionId) {
        ProctoringReport report = proctoringService.generateReport(sessionId);
        if (report != null) {
            return ResponseEntity.ok(report);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Video Proctoring API",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}
