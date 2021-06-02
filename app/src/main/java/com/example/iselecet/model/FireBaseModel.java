package com.example.iselecet.model;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.iselecet.model.user.Doctor;
import com.example.iselecet.model.user.Patient;
import com.example.iselecet.model.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FireBaseModel {

    public FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public FirebaseStorage storage = FirebaseStorage.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void signUpToFireBase (User user, String password, Activity activity){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user.setId(mAuth.getCurrentUser().getUid());
                            Model.instance.addUser(user,()->{ });
                            Toast.makeText(activity, "User Created Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("failedSignup", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity, "User Failed To Create", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void addUser(User user, final Model.AddUserListener listener) {
        if(user.getRole().equals("Doctor")){
            db.collection("Doctors").document(user.getId())
                    .set(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG","User added successfully");
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG","fail adding User");
                    listener.onComplete();
                }
            });
        }
        else{
            db.collection("Patients").document(user.getId())
                    .set(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG","User added successfully");
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG","fail adding User");
                    listener.onComplete();
                }
            });
        }

    }


    public void getUserRole(String userId, Model.StringListener listener) {

        db.collection("Doctors").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()){
                                String role = doc.getString("role");
                                listener.onComplete(role);
                            }
                            else{
                                db.collection("Patients").document(userId).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot doc = task.getResult();
                                                    String role = doc.getString("role");
                                                    listener.onComplete(role);
                                                }
                                            }
                                        });
                            }

                        }
                    }
                });




    }

    public void logInToFireBase (String email, String password, Activity activity, Model.SuccessListener listener){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Toast.makeText(activity, "Sign In was Successfully", Toast.LENGTH_SHORT).show();
                            listener.onComplete(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            listener.onComplete(false);
                        }
                    }
                });
    }

    public void signOutFromFireBase (){
        mAuth.signOut();
    }

    public String getCurrentUserId(){
        return mAuth.getCurrentUser().getUid();
    }

    public void getAllDoctors(final Model.ListListener listener){
        ArrayList<Doctor> doctorsList = new ArrayList<>();
        db.collection("Doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty() == false) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Doctor doctor = new Doctor();
                            doctor.fromMap(doc.getData());
                            doctorsList.add(doctor);
                            Log.d("TAG","game: " + doctor.getId());
                        }
                    }
                }
                listener.onComplete(doctorsList);
            }
        });
    }

    public void updateDoctor(String doctorId, Map<String,Object> map, Model.SuccessListener listener) {
        db.collection("Doctors").document(doctorId)
                .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onComplete(true);
                } else {
                    listener.onComplete(false);
                }
            }
        });
    }

    public void getPatientData(String patientId, Model.PatientListener listener) {
        db.collection("Patients").document(patientId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            Patient patient = new Patient();
                            patient.fromMap(doc.getData());
                            listener.onComplete(patient);
                        }
                    }
                });
    }

    public void getDoctorData(String doctorId, Model.DoctorListener listener) {
        db.collection("Doctors").document(doctorId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            Doctor doctor = new Doctor();
                            doctor.fromMap(doc.getData());
                            listener.onComplete(doctor);
                        }
                    }
                });
    }

    public void getDoctorWaitingList(String doctorId, Model.ListListener listener) {
        ArrayList<Patient> patientList = new ArrayList<>();
        db.collection("Doctors").document(doctorId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            ArrayList<HashMap<String,String>> hashList = (ArrayList<HashMap<String, String>>) document.get("patientList");
                            for(HashMap<String,String> m:hashList){
                                Patient tmpPatient = new Patient(m.get("email"),m.get("role"),m.get("fullName"));
                                tmpPatient.setId(m.get("id"));
                                patientList.add(tmpPatient);
                            }
                        }
                        listener.onComplete(patientList);
                    }
                });
    }

    public void isPatientExist(String doctorId, String patientId, Model.SuccessListener listener) {
        db.collection("Doctors").document(doctorId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Doctor doctor = new Doctor();
                            doctor.fromMap(document.getData());
                            boolean flag = false;
                            if(doctor.getCurrentPatient()!=null){
                                if(doctor.getCurrentPatient().get("id").equals(patientId)){
                                    flag = true;
                            }
                           }
                            ArrayList<HashMap<String,String>> hashList = (ArrayList<HashMap<String, String>>) document.get("patientList");
                            for(HashMap<String,String> patient:hashList){
                                if(patient.get("id").equals(patientId)){
                                    flag = true;
                                }

                            }
                            if(flag){
                                listener.onComplete(true);
                            }
                            else{
                                listener.onComplete(false);
                            }
                        }

                    }
                });
    }





}
