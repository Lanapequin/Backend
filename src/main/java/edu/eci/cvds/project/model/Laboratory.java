package edu.eci.cvds.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Laboratory")
public class Laboratory {
    @Id
    private String id;
    private String name;

    public Laboratory() {}

    public Laboratory(String name, String location, int capacity) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}