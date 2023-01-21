package com.example.kisanseeva;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {

    static CollectionReference getCollectionReferenceForApplication(String id) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Product")
                .document(id).collection("Application");
    }


    public static CollectionReference getCollectionReferenceForProduct() {
        return FirebaseFirestore.getInstance().collection("Product")
                .document("1").collection("rentedTractor");
    }

    // To get user's data
    static DocumentReference getCollectionReferenceForUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("User")
                .document(currentUser.getUid());
    }

}
