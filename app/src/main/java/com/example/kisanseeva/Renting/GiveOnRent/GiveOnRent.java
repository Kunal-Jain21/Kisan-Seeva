package com.example.kisanseeva.Renting.GiveOnRent;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.R;
import com.example.kisanseeva.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.ArrayList;

public class GiveOnRent extends Fragment {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    ArrayList<ProductModel> rentedProduct;
    ProductListAdapter productListAdapter;

    public static GiveOnRent newInstance(String param1, String param2) {
        GiveOnRent fragment = new GiveOnRent();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_give_on_rent, container, false);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.rented_product_list);

        // Fab button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddProduct.class));
            }
        });

        rentedProduct = new ArrayList<>();
        // Recycler View
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        productListAdapter = new ProductListAdapter(getContext(), rentedProduct);
        recyclerView.setAdapter(productListAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        setData();
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        setData();
//    }

    private void setData() {
        rentedProduct.clear();

        Utility.getDocumentReferenceOfUser().collection("my_product").get()
                .addOnSuccessListener(documentSnapshots -> {

                    for (DocumentSnapshot documentSnapshot: documentSnapshots.getDocuments()) {
                        String id = documentSnapshot.get("prodId").toString();
                        Utility.getCollectionReferenceForRentedProduct().document(id)
                                .get().addOnSuccessListener(documentSnapshot1 -> {
                            ProductModel curr = documentSnapshot1.toObject(ProductModel.class);
//                            ProductModel sample = new ProductModel();
//                            sample.setProd_name(curr.getProd_name());
//                            sample.setProd_price(curr.getProd_price());
//                            sample.setProd_desc(curr.getProd_desc());
//                            sample.setProd_img(curr.getProd_img());
                            rentedProduct.add(curr);
                            productListAdapter.notifyDataSetChanged();
                                });
                    }
                });
    }
}