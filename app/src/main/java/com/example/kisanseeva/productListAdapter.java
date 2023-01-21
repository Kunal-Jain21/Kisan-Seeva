package com.example.kisanseeva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.Renting.GiveOnRent.productModel;

import java.util.ArrayList;

public class productListAdapter extends RecyclerView.Adapter<productListAdapter.productAdapter> {

    Context context;
    ArrayList<productModel> produtList;

    public productListAdapter(Context context, ArrayList<productModel> produtList) {
        this.context = context;
        this.produtList = produtList;
    }

    @NonNull
    @Override
    public productAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new productAdapter(view);

    }

    @Override
    public void onBindViewHolder(@NonNull productAdapter holder, int position) {
        productModel curr = produtList.get(position);
        holder.prod_name.setText(curr.getProd_name());
        holder.prod_desc.setText(curr.getProd_desc());
        holder.prod_price.setText(String.valueOf(curr.getProd_price()));
        holder.product_img.setImageResource(curr.getProd_img());
    }

    @Override
    public int getItemCount() {
        return produtList.size();
    }

    public class productAdapter extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView prod_name, prod_desc, prod_price;
        CardView product_item;
        public productAdapter(@NonNull View view) {
            super(view);
            product_img = view.findViewById(R.id.product_img);
            prod_name = view.findViewById(R.id.prod_name);
            prod_desc = view.findViewById(R.id.prod_desc);
            prod_price = view.findViewById(R.id.prod_price);
            product_item = view.findViewById(R.id.product_item);
        }
    }
}
