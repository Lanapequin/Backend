package edu.eci.cvds.project.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "Laboratory")
public class Laboratory {
    @Id
    private String id;
    private String name;
    private List<Reservation> reservations;

    public Laboratory() {
    }

    public Laboratory(String name, String location, int capacity) {
        this.name = name;
    }
}