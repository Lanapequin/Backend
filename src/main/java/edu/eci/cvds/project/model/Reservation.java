package edu.eci.cvds.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "Reservation")
public class Reservation {
    @Id
    private String id;
    private String laboratoryId;
    private String userId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String purpose;

    public Reservation() {}

    public Reservation(String laboratoryId, String user, LocalDateTime startDateTime, LocalDateTime endDateTime, String purpose) {
        this.laboratoryId = laboratoryId;
        this.userId = user;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.purpose = purpose;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLaboratoryId() {
        return laboratoryId;
    }
    public void setLaboratoryId(String laboratoryId) {
        this.laboratoryId = laboratoryId;
    }
    public String getUser() {
        return userId;
    }
    public void setUser(String user) {
        this.userId = user;
    }
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
