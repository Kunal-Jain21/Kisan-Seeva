package com.example.kisanseeva.Renting.TakeOnRent;

import android.annotation.SuppressLint;
import android.os.Bundle;

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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_product_list);

        recycle = findViewById(R.id.rented_product_list);
        equipmentName = getIntent().getStringExtra("equipmentName");
        rentedProduct = new ArrayList<>();

        setRecyclerView();
//        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void setRecyclerView() {
        recycle.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this, rentedProduct);
        recycle.setAdapter(productListAdapter);
    }

    private void getData() {
        rentedProduct.clear();

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
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                ProductModel curr = documentSnapshot.toObject(ProductModel.class);
                                if (!requestList.contains(curr.getProd_id())) {
                                    rentedProduct.add(curr);
                                    productListAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
    }
}