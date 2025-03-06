package edu.eci.cvds.project.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
@Getter
@Setter
@Document(collection = "Reservation")
public class Reservation {
    @Id
    private String id;
    private Laboratory laboratory;
    private User user;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String purpose;
    private boolean status;

    public Reservation() {}

    public Reservation(Laboratory laboratory, User user, LocalDateTime startDateTime, LocalDateTime endDateTime, String purpose,boolean status) {
        this.laboratory = laboratory;
        this.user = user;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.purpose = purpose;
        this.status = status;
    }
}
