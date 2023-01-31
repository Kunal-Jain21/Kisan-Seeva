package com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.kisanseeva.R;

import java.util.ArrayList;

public class ProductRequestActivity extends AppCompatActivity {

    RecyclerView person_request_list;
    RequestListAdapter requestListAdapter;
    ArrayList<Person> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_request);


        requestList = new ArrayList<>();
        requestList.add(new Person("Kunal", R.drawable.hand_tools));
        requestList.add(new Person("Kunal", R.drawable.hand_tools));
        requestList.add(new Person("Kunal", R.drawable.hand_tools));
        requestList.add(new Person("Kunal", R.drawable.hand_tools));
        person_request_list = findViewById(R.id.person_request_list);
        requestListAdapter = new RequestListAdapter(this, requestList);
        person_request_list.setAdapter(requestListAdapter);
        requestListAdapter.notifyDataSetChanged();
    }
}