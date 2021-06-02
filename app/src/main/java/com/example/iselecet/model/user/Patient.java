package com.example.iselecet.model.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Patient extends User{

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    private Boolean isWaiting = false;
    private long arrivalTime;


    public Patient(){

    }
    public Patient(String email, String role, String fullName) {
        super(email, role, fullName);
    }


    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("email", getEmail());
        result.put("fullName", getFullName());
        result.put("role", getRole());
        result.put("isWaiting",isWaiting);
        return result;
    }

    @Override
    public void fromMap(Map<String, Object> map) {
        this.setId((String)map.get("id"));
        this.setEmail((String)map.get("email"));
        this.setFullName((String)map.get("fullName"));
        this.setRole((String)map.get("role"));
        this.isWaiting = (Boolean)map.get("isWaiting");
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getWaiting() {
        return isWaiting;
    }

    public void setWaiting(Boolean waiting) {
        isWaiting = waiting;
    }
}
