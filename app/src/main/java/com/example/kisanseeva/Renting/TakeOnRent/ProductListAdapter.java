package com.example.kisanseeva.Renting.TakeOnRent;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.R;
import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.example.kisanseeva.Utility;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.productAdapter> {

    Context context;
    ArrayList<ProductModel> productList;

    public ProductListAdapter(Context context, ArrayList<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public productAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new productAdapter(view);

    }

    @Override
    public void onBindViewHolder(@NonNull productAdapter holder, int position) {
        ProductModel curr = productList.get(position);
        holder.prod_name.setText(curr.getProd_name());
        holder.prod_desc.setText(curr.getProd_desc());
        holder.prod_price.setText(String.valueOf(curr.getProd_price()));
        Glide.with(context)
                .load(curr.getProd_img())
                .into(holder.product_img);
        holder.product_img.setImageURI(Uri.parse(curr.getProd_img()));
        holder.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference applicationReference =Utility.getCollectionReferenceForRentedProduct()
                        .document(curr.getProd_id()).collection("requestApplication").document();
                applicationReference.set(new HashMap<String,Object>(){{
                    put("requestUserId",Utility.getCurrentUser().getUid());
                    put("isApproved",false);
                }
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DocumentReference sentRequestReference = Utility.getCollectionReferenceForSentRequest().document(applicationReference.getId());
                        sentRequestReference.set(new HashMap<String,String>(){{
                            put("productId",curr.getProd_id());
                        }}).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context,"Failure in adding sent request",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(context,"Failure in adding request application",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class productAdapter extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView prod_name, prod_desc, prod_price;
        CardView product_item;

        MaterialButton requestButton;

        public productAdapter(@NonNull View view) {
            super(view);
            product_img = view.findViewById(R.id.product_img);
            prod_name = view.findViewById(R.id.prod_name);
            prod_desc = view.findViewById(R.id.prod_desc);
            prod_price = view.findViewById(R.id.prod_price);
            product_item = view.findViewById(R.id.product_item);
            requestButton = view.findViewById(R.id.requestButton);
        }
    }
}