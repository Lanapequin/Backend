package edu.eci.cvds.project.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "User")
public class User {
    private String id;
    private String username;
    private String password;
    public List<Reservation> reservations;
    private Role role;
}


