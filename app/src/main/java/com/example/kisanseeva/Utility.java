package com.example.kisanseeva;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class Utility {

    // For getting list of application for a product
    static CollectionReference getCollectionReferenceForApplication(String id) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Product")
                .document(id).collection("Application");
    }

    // getting list of product according to category
    public static CollectionReference getCollectionReferenceForRentedProduct() {
        return FirebaseFirestore.getInstance().collection("Product")
                .document("1").collection("rentedTractor");
    }

    // To get user's data
    public static DocumentReference getDocumentReferenceOfUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("User")
                .document(currentUser.getUid());
    }

    // Reference to add Image in Database
    public static StorageReference getStorageReferenceForImage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Image1" + new Random().nextInt(50));
        return storageReference;
    }

}
