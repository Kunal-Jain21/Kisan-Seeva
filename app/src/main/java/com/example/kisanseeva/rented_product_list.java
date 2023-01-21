package com.example.kisanseeva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.kisanseeva.Renting.GiveOnRent.productModel;

import java.util.ArrayList;

public class rented_product_list extends AppCompatActivity {

    RecyclerView recycle;
    productListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_product_list);

        ArrayList<productModel> rentedProduct = new ArrayList<>();
        rentedProduct.add(new productModel("1", "Tractor", "rent", 250, R.drawable.leaf_blower));
        rentedProduct.add(new productModel("2", "Tractor", "rent", 250, R.drawable.leaf_blower));
        rentedProduct.add(new productModel("3", "Tractor", "rent", 250, R.drawable.leaf_blower));
        rentedProduct.add(new productModel("1", "Tractor", "rent", 250, R.drawable.leaf_blower));
        rentedProduct.add(new productModel("2", "Tractor", "rent", 250, R.drawable.leaf_blower));
        rentedProduct.add(new productModel("3", "Tractor", "rent", 250, R.drawable.leaf_blower));

        recycle = findViewById(R.id.rented_product_list);
        productListAdapter = new productListAdapter(this, rentedProduct);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();
    }
}