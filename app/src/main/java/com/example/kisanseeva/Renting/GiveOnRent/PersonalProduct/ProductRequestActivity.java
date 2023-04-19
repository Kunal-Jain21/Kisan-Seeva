package com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.R;
import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.example.kisanseeva.Utility;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProductRequestActivity extends AppCompatActivity implements AcceptOrRejectApplication {

    RecyclerView person_request_list;
    RequestListAdapter requestListAdapter;
    ArrayList<Person> requesterList;
    String prod_id;
    private ImageView product_img;
    TextView prod_name, prod_desc, prod_price, emptyRequestTv;
    private ArrayList<String> applicationIdList, applicationStatusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_request);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        product_img = findViewById(R.id.product_img);

        prod_id = getIntent().getStringExtra("prod_id");
        prod_name = findViewById(R.id.prod_name);
        prod_desc = findViewById(R.id.prod_desc);
        prod_price = findViewById(R.id.prod_price);
        product_img = findViewById(R.id.product_img);
        emptyRequestTv = findViewById(R.id.emptyRequestTv);
        setData();
        requesterList = new ArrayList<>();
        applicationIdList = new ArrayList<>();
        applicationStatusList = new ArrayList<>();
        getData();

        person_request_list = findViewById(R.id.person_request_list);
        requestListAdapter = new RequestListAdapter(this, requesterList, this, applicationStatusList);
        person_request_list.setLayoutManager(new LinearLayoutManager(this));
        person_request_list.setAdapter(requestListAdapter);
        requestListAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    void setData() {
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
            if (documentSnapshots.isEmpty()) {
                emptyRequestTv.setText("No Request for this Product");
                emptyRequestTv.setVisibility(View.VISIBLE);
            } else {
                for (DocumentSnapshot doc : documentSnapshots) {


                    applicationStatusList.add((String) doc.get("isApproved"));
                    applicationIdList.add(doc.getId());
                    Utility.getDocumentReferenceOfUser(String.valueOf(doc.get("requestUserId")))
                            .get().addOnSuccessListener(documentSnapshot -> {
                                requesterList.add(documentSnapshot.toObject(Person.class));
                                requestListAdapter.notifyDataSetChanged();
                            });
                }
            }
        });
    }


    @Override
    public void acceptButtonListener(int position) {
        Utility.getCollectionReferenceForApplication(prod_id).
                document(applicationIdList.get(position)).update("isApproved", "true").addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Approved Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void rejectButtonListener(int position) {
        Utility.getCollectionReferenceForApplication(prod_id).
                document(applicationIdList.get(position)).update("isApproved", "false").addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Offer Declined", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}