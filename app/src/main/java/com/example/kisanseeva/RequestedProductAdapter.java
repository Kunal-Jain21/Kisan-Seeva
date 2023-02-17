package com.example.kisanseeva;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;

import java.util.ArrayList;

public class RequestedProductAdapter extends RecyclerView.Adapter<RequestedProductAdapter.RequestedProductViewHolder> {

    Context context;
    ArrayList<ProductModel>  requestedProductList;
    ArrayList<String>  decisionArrayList;

    public RequestedProductAdapter(Context context, ArrayList<ProductModel> requestedProductList, ArrayList<String> decisionArrayList) {
        this.context = context;
        this.requestedProductList = requestedProductList;
        this.decisionArrayList = decisionArrayList;
    }

    @NonNull
    @Override
    public RequestedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.requested_product_item, parent, false);
        return new RequestedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedProductViewHolder holder, int index) {
        ProductModel curr = requestedProductList.get(index);
        Log.v("testing1", curr.getProd_id());
        holder.prod_name.setText(curr.getProd_name());
        holder.prod_desc.setText(curr.getProd_desc());
        holder.prod_price.setText(String.valueOf(curr.getProd_price()));
        Glide.with(context)
                .load(curr.getProd_img())
                .into(holder.prod_img);
        switch (decisionArrayList.get(index)) {
            case "true": {
                holder.statusTextView.setText("Accepted");
                break;
            }
            case "false": {
                holder.statusTextView.setText("Rejected");
                break;
            }
            default:{
                holder.statusTextView.setText("Pending");
            }
        }
    }

    @Override
    public int getItemCount() {
        return requestedProductList.size();
    }

    public static class RequestedProductViewHolder extends RecyclerView.ViewHolder {
        ImageView prod_img;
        TextView prod_name, prod_price, prod_desc, statusTextView;

        public RequestedProductViewHolder(@NonNull View view) {
            super(view);
            prod_img = view.findViewById(R.id.prod_img);
            prod_name = view.findViewById(R.id.prod_name);
            prod_price = view.findViewById(R.id.prod_price);
            prod_desc = view.findViewById(R.id.prod_desc);
            statusTextView = view.findViewById(R.id.statusTextView);
        }
    }

}
