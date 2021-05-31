package com.example.iselecet.model.user;



import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {

    private String id = UUID.randomUUID().toString();
    private String email;
    private String role;
    private String fullName;

    public User(){}

    public User(String email,String role,String fullName){
        this.email = email;
        this.role = role;
        this.fullName = fullName;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("email", email);
        result.put("fullName", fullName);
        result.put("role", role);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
