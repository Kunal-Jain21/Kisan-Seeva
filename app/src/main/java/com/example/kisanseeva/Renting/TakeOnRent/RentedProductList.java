package com.example.kisanseeva.Renting.TakeOnRent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.R;
import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.example.kisanseeva.Utility;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RentedProductList extends AppCompatActivity {

    RecyclerView recycle;
    ProductListAdapter productListAdapter;
    private String equipmentName;
    private ArrayList<ProductModel> rentedProduct;
    ProgressBar progressBar;
    TextView failureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_product_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        recycle = findViewById(R.id.rented_product_list);
        equipmentName = getIntent().getStringExtra("equipmentName");
        rentedProduct = new ArrayList<>();
        progressBar = findViewById(R.id.rentedProgressBar);
        failureText = findViewById(R.id.failureText);
        setRecyclerView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            getData();
            productListAdapter.notifyDataSetChanged();
        }else{
            progressBar.setVisibility(View.GONE);
            failureText.setText("No Internet Connection");
            failureText.setVisibility(View.VISIBLE);
        }

    }

    private void setRecyclerView() {
        recycle.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this, rentedProduct);
        recycle.setAdapter(productListAdapter);
    }

    private void getData() {
        rentedProduct.clear();
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<String> requestList = new ArrayList<>();
        Utility.getCollectionReferenceForSentRequest().get().addOnSuccessListener(documentSnapshot -> {
            for (DocumentSnapshot docSnap : documentSnapshot) {
                requestList.add(String.valueOf(docSnap.get("productId")));
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.getCollectionReferenceForRentedProduct().whereEqualTo("category", equipmentName)
                        .whereNotEqualTo("giver_id", Utility.getCurrentUser().getUid())
                        .get().addOnSuccessListener(documentSnapshots -> {
                            if (documentSnapshots.isEmpty()) {
                                Log.v("testing", "Line 70");
                                progressBar.setVisibility(View.INVISIBLE);
                                failureText.setText("No product added by other");
                                failureText.setVisibility(View.VISIBLE);
                            } else {
                                Log.v("testing", "Line 74");
                                failureText.setVisibility(View.INVISIBLE);
                                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                    ProductModel curr = documentSnapshot.toObject(ProductModel.class);
                                    if (!requestList.contains(curr.getProd_id())) {
                                        rentedProduct.add(curr);
                                        productListAdapter.notifyDataSetChanged();
                                    }
                                }
                                if(rentedProduct.size() == 0) {
                                    failureText.setText("No product added by other");
                                    failureText.setVisibility(View.VISIBLE);
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });
    }
}