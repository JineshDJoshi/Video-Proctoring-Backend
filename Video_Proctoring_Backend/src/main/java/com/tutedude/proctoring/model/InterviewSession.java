package com.tutedude.proctoring.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class InterviewSession {
    private String sessionId;
    private String candidateName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // ACTIVE, COMPLETED, TERMINATED
    private List<DetectionEvent> detectionEvents;
    private int durationInSeconds;

    // Default constructor
    public InterviewSession() {
        this.detectionEvents = new ArrayList<>();
        this.status = "ACTIVE";
        this.startTime = LocalDateTime.now();
    }

    // Constructor with parameters
    public InterviewSession(String sessionId, String candidateName) {
        this();
        this.sessionId = sessionId;
        this.candidateName = candidateName;
    }

    // Method to add detection event
    public void addDetectionEvent(DetectionEvent event) {
        if (this.detectionEvents == null) {
            this.detectionEvents = new ArrayList<>();
        }
        this.detectionEvents.add(event);
    }

    // Method to end session
    public void endSession() {
        this.endTime = LocalDateTime.now();
        this.status = "COMPLETED";
        if (this.startTime != null && this.endTime != null) {
            this.durationInSeconds = (int) java.time.Duration.between(startTime, endTime).getSeconds();
        }
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DetectionEvent> getDetectionEvents() {
        return detectionEvents;
    }

    public void setDetectionEvents(List<DetectionEvent> detectionEvents) {
        this.detectionEvents = detectionEvents;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    @Override
    public String toString() {
        return "InterviewSession{" +
                "sessionId='" + sessionId + '\'' +
                ", candidateName='" + candidateName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", durationInSeconds=" + durationInSeconds +
                ", detectionEventsCount=" + (detectionEvents != null ? detectionEvents.size() : 0) +
                '}';
    }
}