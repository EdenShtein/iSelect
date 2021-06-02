package com.example.iselecet.model.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Doctor extends User {

    ArrayList<Patient> patientList;
    Boolean isAvailable;
    HashMap currentPatient;

    public Doctor(String email, String role, String fullName) {
        super(email, role, fullName);
        this.isAvailable = true;
        this.patientList = new ArrayList<>();
    }

    public Doctor(){}


    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("email", getEmail());
        result.put("fullName", getFullName());
        result.put("role", getRole());
        result.put("isAvailable",isAvailable);
        result.put("patientList",patientList);
        return result;
    }

    @Override
    public void fromMap(Map<String, Object> map) {
        this.setId((String)map.get("id"));
        this.setEmail((String)map.get("email"));
        this.setFullName((String)map.get("fullName"));
        this.setRole((String)map.get("role"));
        this.isAvailable = (Boolean)map.get("isAvailable");
        this.patientList = (ArrayList<Patient>)map.get("patientList");
        this.currentPatient = (HashMap) map.get("currentPatient");
    }

    public ArrayList<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(ArrayList<Patient> patientList) {
        this.patientList = patientList;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public HashMap getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(HashMap currentPatient) {
        this.currentPatient = currentPatient;
    }
}
