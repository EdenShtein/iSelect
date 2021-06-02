package com.example.iselecet.model;

import android.app.Activity;

import com.example.iselecet.model.user.Doctor;
import com.example.iselecet.model.user.Patient;
import com.example.iselecet.model.user.User;

import java.util.ArrayList;
import java.util.Map;

public class Model {

    private Activity mActivity;
    public final static Model instance = new Model();
    FireBaseModel fireBase = new FireBaseModel();

    public void setActivity(Activity activity){
        this.mActivity = activity;
    }

    public void signUpFB(User user, String password) {
        fireBase.signUpToFireBase(user,password,mActivity);
    }

    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(final User user, final AddUserListener listener) {
        fireBase.addUser(user, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public interface StringListener {
        void onComplete(String data);
    }

    public void getUserRole(String userId, StringListener data) {
        fireBase.getUserRole(userId,data);
    }

    public interface SuccessListener{
        void onComplete(boolean result);
    }
    public void logInFB(String email,String password, SuccessListener listener) {
        fireBase.logInToFireBase(email,password,mActivity, listener);
    }
    public String getCurrentUserId(){
        return fireBase.getCurrentUserId();
    }

    public void signOutFB(){
        fireBase.signOutFromFireBase();
    }

    public interface ListListener<E>{
        void onComplete(ArrayList<E> result);
    }
    public void getAllDoctors(ListListener<Doctor> listener){
        fireBase.getAllDoctors(listener);
    }

    public void updateDoctorAvailable(String doctorId, Map map, SuccessListener listener){
        fireBase.updateDoctor(doctorId,map,listener);
    }

    public interface PatientListener {
        void onComplete(Patient patient);
    }

    public void getPatientData(String patientId, PatientListener listener){
        fireBase.getPatientData(patientId,listener);
    }

    public interface DoctorListener {
        void onComplete(Doctor doctor);
    }

    public void getDoctorData(String doctorId, DoctorListener listener){
        fireBase.getDoctorData(doctorId,listener);
    }

    public void getDoctorWaitingList(String doctorId, ListListener listener){
        fireBase.getDoctorWaitingList(doctorId,listener);
    }

    public void isPatientExist(String doctorId,String patientId,SuccessListener listener){
        fireBase.isPatientExist(doctorId,patientId,listener);
    }


}
