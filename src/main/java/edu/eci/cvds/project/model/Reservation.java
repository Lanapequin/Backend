package edu.eci.cvds.project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "Reservation")
public class Reservation {
    @Id
    private String id;
    private Laboratory laboratory;
    private User user;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String purpose;
    private boolean Status;

    public boolean getStatus() {
        return Status;
    }
}
