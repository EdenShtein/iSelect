package com.example.iselecet.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FireBaseModel {

    public FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public FirebaseStorage storage = FirebaseStorage.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();




}
