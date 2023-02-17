package com.example.kisanseeva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RequestedProductActivity extends AppCompatActivity {

    ArrayList<ProductModel> requestedProductList;
    RecyclerView requestedProductRecyclerView;
    ArrayList<String> decision;
    RequestedProductAdapter requestedProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_product);

        requestedProductRecyclerView = findViewById(R.id.requestedProductRecyclerView);
        requestedProductList = new ArrayList<>();
        decision = new ArrayList<>();

        setData();
        Log.v("testing", "Line 32");
        requestedProductAdapter = new RequestedProductAdapter(this, requestedProductList, decision);
        Log.v("testing", "Line 34");
        requestedProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.v("testing", "Line 36");
        requestedProductRecyclerView.setAdapter(requestedProductAdapter);
        Log.v("testing", "Line 38");

        requestedProductAdapter.notifyDataSetChanged();
    }

    private void setData() {
//        rentedProduct.clear();

        Utility.getCollectionReferenceForSentRequest().get().addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot currentRequestDocument : documentSnapshots) {
                Utility.getCollectionReferenceForRentedProduct().document(currentRequestDocument.get("productId").toString())
                        .get().addOnSuccessListener(currentProduct -> {
                            ProductModel curr = currentProduct.toObject(ProductModel.class);
                            requestedProductList.add(curr);
                        });

                Utility.getCollectionReferenceForApplication(currentRequestDocument.get("productId").toString())
                        .document(currentRequestDocument.getId()).get()
                        .addOnSuccessListener(currentApplication -> {
                            decision.add((String) currentApplication.get("isApproved"));
                            requestedProductAdapter.notifyDataSetChanged();
                        });
            }
        });
    }
}