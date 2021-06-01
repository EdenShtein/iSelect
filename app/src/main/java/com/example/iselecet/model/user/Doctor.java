package com.example.iselecet.model.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Doctor extends User {
    ArrayList<Patient> patientList;
    Boolean isAvailable;

    public Doctor(String email, String role, String fullName) {
        super(email, role, fullName);
        this.isAvailable = true;
    }


    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("email", getEmail());
        result.put("fullName", getFullName());
        result.put("role", getRole());
        result.put("isAvailable",isAvailable);
        return result;
    }
}
