package com.example.kisanseeva.Renting.GiveOnRent;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kisanseeva.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GiveOnRent extends Fragment {

    FloatingActionButton fab;
    RecyclerView recyclerView;

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

        ArrayList<productModel> rentedProduct = new ArrayList<>();
        rentedProduct.add(new productModel("1", "Tractor", "rent", 250, R.drawable.leaf_blower));
        rentedProduct.add(new productModel("2", "Tractor", "rent", 250, R.drawable.leaf_blower));
        rentedProduct.add(new productModel("3", "Tractor", "rent", 250, R.drawable.leaf_blower));
        // Recycler View

        productListAdapter productListAdapter = new productListAdapter(getContext(), rentedProduct);
        Log.v("checking", "Reached line 61");
        recyclerView.setAdapter(productListAdapter);
        Log.v("checking", "Reached line 63");
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        productListAdapter.notifyDataSetChanged();
        return view;
    }
}