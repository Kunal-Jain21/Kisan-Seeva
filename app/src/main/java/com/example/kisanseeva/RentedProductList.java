package com.example.kisanseeva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class RentedProductList extends AppCompatActivity {

    RecyclerView recycle;
    ProductListAdapter productListAdapter;
    private String equipmentName;
    private ArrayList<ProductModel> rentedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_product_list);

        recycle = findViewById(R.id.rented_product_list);

        equipmentName = getIntent().getStringExtra("equipmentName");
        Log.v("EquipName", equipmentName);
        rentedProduct = new ArrayList<>();

        setRecyclerView();
        Log.v("testing", "Line 37");
//        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("testing", "Line 44");
        getData();
    }

    private void setRecyclerView() {
        recycle.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this, rentedProduct);
        recycle.setAdapter(productListAdapter);
    }

    private void getData() {
        Log.v("testing", "Line 55");
        rentedProduct.clear();

        Utility.getCollectionReferenceForRentedProduct().whereEqualTo("category", equipmentName)
                .whereNotEqualTo("giver_id", Utility.getCurrentUser().getUid())
                        .get().addOnSuccessListener(documentSnapshots -> {
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                rentedProduct.add(documentSnapshot.toObject(ProductModel.class));
                                productListAdapter.notifyDataSetChanged();
                            }
                });
    }
}