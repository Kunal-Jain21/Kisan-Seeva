package com.example.kisanseeva.Renting.GiveOnRent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.R;
import com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct.AcceptOrRejectApplication;
import com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct.ProductRequestActivity;
import com.example.kisanseeva.Renting.GiveOnRent.ProductAddition.AddProduct;
import com.example.kisanseeva.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class GiveOnRent extends Fragment implements productListener {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ArrayList<ProductModel> rentedProduct;
    ProductListAdapter productListAdapter;
    TextView emptyPostTv;
    ProgressBar postProgressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_give_on_rent, container, false);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.rented_product_list);
        emptyPostTv = view.findViewById(R.id.emptyPostTv);
        postProgressBar = view.findViewById(R.id.postProgressBar);
        // Fab button
        fab.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), AddProduct.class)));

        rentedProduct = new ArrayList<>();
        // Recycler View
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        productListAdapter = new ProductListAdapter(getContext(), rentedProduct, this);
        recyclerView.setAdapter(productListAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        setData();
    }

    private void setData() {
        rentedProduct.clear();
        postProgressBar.setVisibility(View.VISIBLE);
        Utility.getDocumentReferenceOfUser().collection("my_product").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        postProgressBar.setVisibility(View.INVISIBLE);
                        emptyPostTv.setText("No Product added for Rent");
                        emptyPostTv.setVisibility(View.VISIBLE);
                    } else {
                        for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                            String id = Objects.requireNonNull(documentSnapshot.get("prodId")).toString();
                            Utility.getCollectionReferenceForRentedProduct().document(id)
                                    .get().addOnSuccessListener(documentSnapshot1 -> {
                                        ProductModel curr = documentSnapshot1.toObject(ProductModel.class);
                                        rentedProduct.add(curr);
                                        productListAdapter.notifyDataSetChanged();
                                    });
                        }
                        postProgressBar.setVisibility(View.INVISIBLE);
                        emptyPostTv.setVisibility(View.INVISIBLE);
                    }
                });
    }


    @Override
    public void deleteItem(String personal_prod_id, String prodId) {

        // Delete from requester list
        Utility.getCollectionReferenceForApplication(prodId).get().addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                Utility.getCollectionReferenceForSentRequest((String) documentSnapshot.get("requestUserId")).document(documentSnapshot.getId())
                        .delete().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireActivity(), "Deleted from " + documentSnapshot.get("requestUserId"), Toast.LENGTH_SHORT).show();
                            }
                        });
                documentSnapshot.getReference().delete();
            }
        });

        //Delete from my_product list
        Utility.getDocumentReferenceOfUser().collection("my_product").document(personal_prod_id).delete().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                Toast.makeText(requireActivity(), "Product Deleted from my_product", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete prod details
        Utility.getCollectionReferenceForRentedProduct().document(prodId).get().addOnSuccessListener(documentSnapshot -> {
            String imgUrl = Objects.requireNonNull(documentSnapshot.toObject(ProductModel.class)).getProd_img();
            Utility.getStorageReferenceUsingUrl(imgUrl).delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(requireActivity(), "Product Image Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), "Error in deleting Image", Toast.LENGTH_SHORT).show();
                }
            });
            documentSnapshot.getReference().delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(requireActivity(), "Product Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), "Error in deleting Product", Toast.LENGTH_SHORT).show();
                }
            });
        });
        setData();
    }


    @Override
    public void onItemCLick(String prod_id) {
        Intent intent = new Intent(requireActivity(), ProductRequestActivity.class);
        intent.putExtra("prod_id", prod_id);
        startActivity(intent);
    }

}