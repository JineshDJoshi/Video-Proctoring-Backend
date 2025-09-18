package com.tutedude.proctoring.model;

import java.time.LocalDateTime;



public class DetectionEvent {
    private Long id;
    private String eventType;
    private String message;
    private String severity;
    private LocalDateTime timestamp;
    private String sessionId;

    // Default constructor
    public DetectionEvent() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with parameters
    public DetectionEvent(String eventType, String message, String severity, String sessionId) {
        this.eventType = eventType;
        this.message = message;
        this.severity = severity;
        this.sessionId = sessionId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "DetectionEvent{" +
                "id=" + id +
                ", eventType='" + eventType + '\'' +
                ", message='" + message + '\'' +
                ", severity='" + severity + '\'' +
                ", timestamp=" + timestamp +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}