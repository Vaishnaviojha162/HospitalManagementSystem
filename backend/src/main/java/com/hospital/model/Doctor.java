package com.hospital.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String spec;
    private int docFees;

    public Doctor() {}

    public Doctor(String username, String password, String email, String spec, int docFees) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.spec = spec;
        this.docFees = docFees;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }

    public int getDocFees() { return docFees; }
    public void setDocFees(int docFees) { this.docFees = docFees; }
}
