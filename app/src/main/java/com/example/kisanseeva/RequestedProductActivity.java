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

        requestedProductAdapter = new RequestedProductAdapter(this, requestedProductList, decision);
        requestedProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestedProductRecyclerView.setAdapter(requestedProductAdapter);
        getData();
    }

    private void getData() {
        requestedProductList.clear();

        Utility.getCollectionReferenceForSentRequest().get().addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot currentRequestDocument : documentSnapshots) {
                Utility.getCollectionReferenceForRentedProduct().document((String) currentRequestDocument.get("productId"))
                        .get().addOnSuccessListener(currentProduct -> {
                            ProductModel curr = currentProduct.toObject(ProductModel.class);
//                            Log.v("test", curr.getProd_id());
                            requestedProductList.add(curr);

                            Utility.getCollectionReferenceForApplication(currentRequestDocument.get("productId").toString())
                                    .document(currentRequestDocument.getId()).get()
                                    .addOnSuccessListener(currentApplication -> {
                                        Log.v("test", currentApplication.getId());
                                        decision.add((String) currentApplication.get("isApproved"));
                                        Log.v("testing", requestedProductList.size()+"");
                                        requestedProductAdapter.notifyDataSetChanged();
                                    });
                        });
            }
        });

    }
}