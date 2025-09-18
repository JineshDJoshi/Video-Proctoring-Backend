package com.tutedude.proctoring.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProctoringReport {
    private String sessionId;
    private String candidateName;
    private LocalDateTime sessionDate;
    private int durationInSeconds;
    private int integrityScore;
    private String overallRating;
    private List<DetectionEvent> detectionEvents;
    private Map<String, Integer> eventSummary;
    private int totalEvents;
    private int dangerEvents;
    private int warningEvents;

    // Default constructor
    public ProctoringReport() {
        this.eventSummary = new HashMap<>();
    }

    // Constructor from InterviewSession
    public ProctoringReport(InterviewSession session) {
        this();
        this.sessionId = session.getSessionId();
        this.candidateName = session.getCandidateName();
        this.sessionDate = session.getStartTime();
        this.durationInSeconds = session.getDurationInSeconds();
        this.detectionEvents = session.getDetectionEvents();
        
        calculateMetrics();
    }

    // Method to calculate all metrics
    private void calculateMetrics() {
        if (detectionEvents == null) {
            this.totalEvents = 0;
            this.dangerEvents = 0;
            this.warningEvents = 0;
            this.integrityScore = 100;
            this.overallRating = "Excellent";
            return;
        }

        // Reset counters
        this.eventSummary.clear();
        this.dangerEvents = 0;
        this.warningEvents = 0;

        // Count events by type and severity
        for (DetectionEvent event : detectionEvents) {
            // Count by severity
            if ("DANGER".equalsIgnoreCase(event.getSeverity())) {
                this.dangerEvents++;
            } else if ("WARNING".equalsIgnoreCase(event.getSeverity())) {
                this.warningEvents++;
            }

            // Count by event type
            String eventType = event.getEventType();
            this.eventSummary.put(eventType, 
                this.eventSummary.getOrDefault(eventType, 0) + 1);
        }

        this.totalEvents = detectionEvents.size();

        // Calculate integrity score
        this.integrityScore = calculateIntegrityScore();
        
        // Determine overall rating
        this.overallRating = determineOverallRating();
    }

    private int calculateIntegrityScore() {
        int score = 100;
        
        // Deduct points based on severity
        score -= (this.dangerEvents * 10); // -10 for each danger event
        score -= (this.warningEvents * 5); // -5 for each warning event
        
        // Additional deductions for specific event types
        score -= (this.eventSummary.getOrDefault("MULTIPLE_FACES", 0) * 15);
        score -= (this.eventSummary.getOrDefault("NO_FACE", 0) * 12);
        score -= (this.eventSummary.getOrDefault("PHONE_DETECTED", 0) * 8);
        
        return Math.max(0, score);
    }

    private String determineOverallRating() {
        if (integrityScore >= 90) return "Excellent";
        else if (integrityScore >= 80) return "Very Good";
        else if (integrityScore >= 70) return "Good";
        else if (integrityScore >= 60) return "Fair";
        else if (integrityScore >= 40) return "Poor";
        else return "Very Poor";
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public int getIntegrityScore() {
        return integrityScore;
    }

    public void setIntegrityScore(int integrityScore) {
        this.integrityScore = integrityScore;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public List<DetectionEvent> getDetectionEvents() {
        return detectionEvents;
    }

    public void setDetectionEvents(List<DetectionEvent> detectionEvents) {
        this.detectionEvents = detectionEvents;
        calculateMetrics(); // Recalculate when events are set
    }

    public Map<String, Integer> getEventSummary() {
        return eventSummary;
    }

    public void setEventSummary(Map<String, Integer> eventSummary) {
        this.eventSummary = eventSummary;
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(int totalEvents) {
        this.totalEvents = totalEvents;
    }

    public int getDangerEvents() {
        return dangerEvents;
    }

    public void setDangerEvents(int dangerEvents) {
        this.dangerEvents = dangerEvents;
    }

    public int getWarningEvents() {
        return warningEvents;
    }

    public void setWarningEvents(int warningEvents) {
        this.warningEvents = warningEvents;
    }

    @Override
    public String toString() {
        return "ProctoringReport{" +
                "sessionId='" + sessionId + '\'' +
                ", candidateName='" + candidateName + '\'' +
                ", integrityScore=" + integrityScore +
                ", overallRating='" + overallRating + '\'' +
                ", totalEvents=" + totalEvents +
                ", dangerEvents=" + dangerEvents +
                ", warningEvents=" + warningEvents +
                '}';
    }
}