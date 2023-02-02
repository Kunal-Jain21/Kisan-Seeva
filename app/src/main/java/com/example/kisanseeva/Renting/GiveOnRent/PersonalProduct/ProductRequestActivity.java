package com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.R;
import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.example.kisanseeva.Utility;

import java.util.ArrayList;

public class ProductRequestActivity extends AppCompatActivity {

    RecyclerView person_request_list;
    RequestListAdapter requestListAdapter;
    ArrayList<Person> requestList;
    String prod_id;
    private ImageView product_img;
    TextView prod_name, prod_desc, prod_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_request);

        product_img = findViewById(R.id.product_img);

        prod_id = getIntent().getStringExtra("prod_id");
        prod_name = findViewById(R.id.prod_name);
        prod_desc = findViewById(R.id.prod_desc);
        prod_price = findViewById(R.id.prod_price);
        product_img = findViewById(R.id.product_img);
        setData();
        requestList = new ArrayList<>();
        getData();

//        requestList.add(new Person("Kunal", R.drawable.hand_tools));
//        requestList.add(new Person("Kunal", R.drawable.hand_tools));
//        requestList.add(new Person("Kunal", R.drawable.hand_tools));
//        requestList.add(new Person("Kunal", R.drawable.hand_tools));
        person_request_list = findViewById(R.id.person_request_list);
        requestListAdapter = new RequestListAdapter(this, requestList);
        person_request_list.setLayoutManager(new LinearLayoutManager(this));
        person_request_list.setAdapter(requestListAdapter);
        requestListAdapter.notifyDataSetChanged();
    }

    void setData() {
        Log.v("testing","Line 56");
        Utility.getCollectionReferenceForRentedProduct().document(prod_id)
                .get().addOnSuccessListener(documentSnapshot -> {
                    ProductModel curr = documentSnapshot.toObject(ProductModel.class);
                    prod_name.setText(curr.getProd_name());
                    prod_desc.setText(curr.getProd_desc());
                    prod_price.setText(String.valueOf(curr.getProd_price()));
                    Glide.with(this).load(curr.getProd_img()).into(product_img);
                });
    }

    void getData() {
        Utility.getCollectionReferenceForApplication(prod_id).get().addOnSuccessListener(documentSnapshots -> {
            requestList.addAll(documentSnapshots.toObjects(Person.class));
            requestListAdapter.notifyDataSetChanged();
        });

            Log.v("test",""+requestList.size());
            Log.v("test",requestList.get(0).getName());
    }
}