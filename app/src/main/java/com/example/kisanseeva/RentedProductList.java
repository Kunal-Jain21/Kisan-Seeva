package com.example.kisanseeva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;

import java.util.ArrayList;

public class RentedProductList extends AppCompatActivity {

    RecyclerView recycle;
    ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_product_list);

        ArrayList<ProductModel> rentedProduct = new ArrayList<>();
        ProductModel temp = new ProductModel();
        temp.setProd_id("1");
        temp.setProd_name("Tractor");
        temp.setProd_desc("rent");
        temp.setProd_price(250);
        temp.setProd_img("https://firebasestorage.googleapis.com/v0/b/kisan-seeva-6c8fd.appspot.com/o/Image137?alt=media&token=f22965bd-1fc0-4425-919b-95abdd6e43e2");
        rentedProduct.add(temp);
        rentedProduct.add(temp);
        rentedProduct.add(temp);

        recycle = findViewById(R.id.rented_product_list);
        productListAdapter = new ProductListAdapter(this, rentedProduct);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();
    }
}