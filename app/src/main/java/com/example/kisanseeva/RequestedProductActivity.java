package com.example.kisanseeva;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RequestedProductActivity extends AppCompatActivity {

    ArrayList<ProductModel> requestedProductList;
    RecyclerView requestedProductRecyclerView;
    ArrayList<String> decision;
    RequestedProductAdapter requestedProductAdapter;
    ProgressBar progressBar;
    TextView failureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_product);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        requestedProductRecyclerView = findViewById(R.id.requestedProductRecyclerView);
        progressBar = findViewById(R.id.rentedProgressBar);
        failureText = findViewById(R.id.failureText);
        requestedProductList = new ArrayList<>();
        decision = new ArrayList<>();

        requestedProductAdapter = new RequestedProductAdapter(this, requestedProductList, decision);
        requestedProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestedProductRecyclerView.setAdapter(requestedProductAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            getData();
            requestedProductAdapter.notifyDataSetChanged();
        }else{
            progressBar.setVisibility(View.GONE);
            failureText.setText("No Internet Connection");
            failureText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void getData() {
        requestedProductList.clear();

        Utility.getCollectionReferenceForSentRequest().get().addOnSuccessListener(documentSnapshots -> {
            if (documentSnapshots.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                failureText.setText("No Request Made");
                failureText.setVisibility(View.VISIBLE);
            }else {
                for (DocumentSnapshot currentRequestDocument : documentSnapshots) {
                    Utility.getCollectionReferenceForRentedProduct().document((String) currentRequestDocument.get("productId"))
                            .get().addOnSuccessListener(currentProduct -> {
                                ProductModel curr = currentProduct.toObject(ProductModel.class);
                                requestedProductList.add(curr);

                                Utility.getCollectionReferenceForApplication(currentRequestDocument.get("productId").toString())
                                        .document(currentRequestDocument.getId()).get()
                                        .addOnSuccessListener(currentApplication -> {
                                            Log.v("test", currentApplication.getId());
                                            decision.add((String) currentApplication.get("isApproved"));
                                            Log.v("testing", requestedProductList.size() + "");
                                            requestedProductAdapter.notifyDataSetChanged();
                                        });
                            });
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}