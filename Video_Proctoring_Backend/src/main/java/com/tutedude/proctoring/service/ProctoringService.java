package com.tutedude.proctoring.service;

import com.tutedude.proctoring.model.DetectionEvent;
import com.tutedude.proctoring.model.InterviewSession;
import com.tutedude.proctoring.model.ProctoringReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ProctoringService {

    // In-memory storage for demo purposes
    // In production, use a database
    private final Map<String, InterviewSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, List<DetectionEvent>> sessionEvents = new ConcurrentHashMap<>();

    /**
     * Start a new interview session
     */
    public String startSession(String candidateName) {
        if (candidateName == null || candidateName.trim().isEmpty()) {
            throw new IllegalArgumentException("Candidate name cannot be null or empty");
        }

        String sessionId = generateSessionId();
        InterviewSession session = new InterviewSession(sessionId, candidateName.trim());
        sessions.put(sessionId, session);
        sessionEvents.put(sessionId, new ArrayList<>());

        System.out.println("Started new interview session: " + sessionId + " for candidate: " + candidateName);
        return sessionId;
    }

    /**
     * End an interview session
     */
    public InterviewSession endSession(String sessionId) {
        InterviewSession session = sessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found: " + sessionId);
        }

        session.endSession();
        
        // Add all events to the session
        List<DetectionEvent> events = sessionEvents.get(sessionId);
        if (events != null) {
            session.setDetectionEvents(new ArrayList<>(events));
        }

        System.out.println("Ended interview session: " + sessionId);
        return session;
    }

    /**
     * Add a detection event to a session
     */
    public void addDetectionEvent(String sessionId, String eventType, String message, String severity) {
        InterviewSession session = sessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found: " + sessionId);
        }

        if (!"ACTIVE".equals(session.getStatus())) {
            throw new IllegalStateException("Cannot add events to inactive session: " + sessionId);
        }

        DetectionEvent event = new DetectionEvent(eventType, message, severity, sessionId);
        event.setId(System.currentTimeMillis()); // Simple ID generation
        
        // Add to session events
        sessionEvents.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(event);
        
        System.out.println("Added detection event to session " + sessionId + ": " + eventType + " - " + severity);
    }

    /**
     * Get a specific session
     */
    public InterviewSession getSession(String sessionId) {
        InterviewSession session = sessions.get(sessionId);
        if (session != null) {
            // Add current events to session
            List<DetectionEvent> events = sessionEvents.get(sessionId);
            if (events != null) {
                session.setDetectionEvents(new ArrayList<>(events));
            }
        }
        return session;
    }

    /**
     * Get all sessions
     */
    public List<InterviewSession> getAllSessions() {
        List<InterviewSession> allSessions = new ArrayList<>();
        for (InterviewSession session : sessions.values()) {
            // Add current events to each session
            List<DetectionEvent> events = sessionEvents.get(session.getSessionId());
            if (events != null) {
                session.setDetectionEvents(new ArrayList<>(events));
            }
            allSessions.add(session);
        }
        return allSessions;
    }

    /**
     * Generate a proctoring report for a session
     */
    public ProctoringReport generateReport(String sessionId) {
        InterviewSession session = sessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found: " + sessionId);
        }

        // Ensure session has all events
        List<DetectionEvent> events = sessionEvents.get(sessionId);
        if (events != null) {
            session.setDetectionEvents(new ArrayList<>(events));
        }

        ProctoringReport report = new ProctoringReport(session);
        
        System.out.println("Generated report for session: " + sessionId + 
                          " - Integrity Score: " + report.getIntegrityScore());
        
        return report;
    }

    /**
     * Delete a session and its events
     */
    public boolean deleteSession(String sessionId) {
        InterviewSession removedSession = sessions.remove(sessionId);
        sessionEvents.remove(sessionId);
        
        boolean deleted = removedSession != null;
        if (deleted) {
            System.out.println("Deleted session: " + sessionId);
        }
        return deleted;
    }

    /**
     * Get session statistics
     */
    public Map<String, Object> getSessionStatistics(String sessionId) {
        InterviewSession session = sessions.get(sessionId);
        List<DetectionEvent> events = sessionEvents.get(sessionId);
        
        if (session == null) {
            return null;
        }

        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("sessionId", sessionId);
        stats.put("candidateName", session.getCandidateName());
        stats.put("status", session.getStatus());
        stats.put("startTime", session.getStartTime());
        stats.put("endTime", session.getEndTime());
        stats.put("totalEvents", events != null ? events.size() : 0);
        
        if (events != null) {
            long dangerEvents = events.stream()
                    .filter(e -> "DANGER".equalsIgnoreCase(e.getSeverity()))
                    .count();
            long warningEvents = events.stream()
                    .filter(e -> "WARNING".equalsIgnoreCase(e.getSeverity()))
                    .count();
            
            stats.put("dangerEvents", dangerEvents);
            stats.put("warningEvents", warningEvents);
        } else {
            stats.put("dangerEvents", 0);
            stats.put("warningEvents", 0);
        }
        
        return stats;
    }

    /**
     * Generate a unique session ID
     */
    private String generateSessionId() {
        return "SESSION_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() + "_" + 
               System.currentTimeMillis() % 10000;
    }

    /**
     * Get system statistics
     */
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("totalSessions", sessions.size());
        
        long activeSessions = sessions.values().stream()
                .filter(s -> "ACTIVE".equals(s.getStatus()))
                .count();
        
        long completedSessions = sessions.values().stream()
                .filter(s -> "COMPLETED".equals(s.getStatus()))
                .count();
        
        stats.put("activeSessions", activeSessions);
        stats.put("completedSessions", completedSessions);
        stats.put("timestamp", LocalDateTime.now());
        
        return stats;
    }

    /**
     * Cleanup inactive sessions (for maintenance)
     */
    public int cleanupInactiveSessions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24); // Remove sessions older than 24 hours
        
        List<String> sessionsToRemove = new ArrayList<>();
        for (Map.Entry<String, InterviewSession> entry : sessions.entrySet()) {
            InterviewSession session = entry.getValue();
            if (session.getStartTime().isBefore(cutoffTime) && 
                !"ACTIVE".equals(session.getStatus())) {
                sessionsToRemove.add(entry.getKey());
            }
        }
        
        for (String sessionId : sessionsToRemove) {
            sessions.remove(sessionId);
            sessionEvents.remove(sessionId);
        }
        
        System.out.println("Cleaned up " + sessionsToRemove.size() + " inactive sessions");
        return sessionsToRemove.size();
    }
}