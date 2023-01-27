package com.example.kisanseeva.Renting.GiveOnRent;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kisanseeva.R;
import com.example.kisanseeva.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

public class GiveOnRent extends Fragment {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    ArrayList<productModel> rentedProduct;
    productListAdapter productListAdapter;

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
                startActivity(new Intent(getActivity(), addProduct.class));
            }
        });

        rentedProduct = new ArrayList<>();
//        setData();
        productModel temp = new productModel();
        temp.setProd_id("1");
        temp.setProd_name("Tractor");
        temp.setProd_desc("rent");
        temp.setProd_price(250);
        temp.setProd_img("https://firebasestorage.googleapis.com/v0/b/kisan-seeva-6c8fd.appspot.com/o/Image137?alt=media&token=f22965bd-1fc0-4425-919b-95abdd6e43e2");
        rentedProduct.add(temp);
        rentedProduct.add(temp);
        rentedProduct.add(temp);
        // Recycler View

        productListAdapter = new productListAdapter(getContext(), rentedProduct);
        recyclerView.setAdapter(productListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        setData();
//    }


    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        rentedProduct.clear();
        Utility.getDocumentReferenceOfUser().collection("productRented").
                get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                return;
            }
            else {
                rentedProduct.addAll(queryDocumentSnapshots.toObjects(productModel.class));
                productListAdapter.notifyDataSetChanged();
            }
        });
    }
}