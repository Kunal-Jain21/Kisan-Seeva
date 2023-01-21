package com.example.kisanseeva.Renting.GiveOnRent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class productListAdapter extends RecyclerView.Adapter<productListAdapter.productAdapter> {
    Context context;
    ArrayList<productModel> rentedProduct;

    public productListAdapter(Context context, ArrayList<productModel> rentedProduct) {
        this.context = context;
        this.rentedProduct = rentedProduct;
    }

    @NonNull
    @Override
    public productAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rented_product_list_item, parent, false);
        return new productAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productAdapter holder, int position) {
        productModel curr = rentedProduct.get(position);
        holder.prod_name.setText(curr.getProd_name());
        holder.prod_desc.setText(curr.getProd_desc());
        holder.prod_price.setText(String.valueOf(curr.getProd_price()));
        Log.v("checking", "Reached inside onBindViewHolder");
        holder.prod_img.setImageResource(curr.getProd_img());
    }

    @Override
    public int getItemCount() {
        return rentedProduct.size();
    }

    public class productAdapter extends RecyclerView.ViewHolder {
        ImageView prod_img;
        TextView prod_name, prod_price, prod_desc;
        public productAdapter(@NonNull View view) {
            super(view);
            Log.v("checking", "Reached inside viewholder");
            prod_img = view.findViewById(R.id.prod_img);
            prod_name = view.findViewById(R.id.prod_name);
            prod_price = view.findViewById(R.id.prod_price);
            prod_desc = view.findViewById(R.id.prod_desc);
        }
    }


}
