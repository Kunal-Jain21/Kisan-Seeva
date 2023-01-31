package com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.R;
import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.example.kisanseeva.Renting.GiveOnRent.productListener;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.productAdapter> {
    Context context;
    ArrayList<ProductModel> rentedProduct;
    com.example.kisanseeva.Renting.GiveOnRent.productListener productListener;

    public ProductListAdapter(Context context, ArrayList<ProductModel> rentedProduct, productListener productListener) {
        this.context = context;
        this.rentedProduct = rentedProduct;
        this.productListener = productListener;
    }

    @NonNull
    @Override
    public productAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rented_product_list_item, parent, false);
        return new productAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productAdapter holder, int position) {
        ProductModel curr = rentedProduct.get(position);
        holder.prod_name.setText(curr.getProd_name());
        holder.prod_desc.setText(curr.getProd_desc());
        holder.prod_price.setText(String.valueOf(curr.getProd_price()));
        Glide.with(context)
                .load(curr.getProd_img())
                .into(holder.prod_img);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productListener.deleteItem(curr.getPersonal_prod_id(),curr.getProd_id());
                rentedProduct.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        holder.productItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productListener.onItemCLick(curr.getProd_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return rentedProduct.size();
    }



    public class productAdapter extends RecyclerView.ViewHolder {
        ImageView prod_img;
        TextView prod_name, prod_price, prod_desc;
        ImageButton deleteButton;
        LinearLayout productItem;

        public productAdapter(@NonNull View view) {
            super(view);
            prod_img = view.findViewById(R.id.prod_img);
            prod_name = view.findViewById(R.id.prod_name);
            prod_price = view.findViewById(R.id.prod_price);
            prod_desc = view.findViewById(R.id.prod_desc);
            deleteButton = view.findViewById(R.id.deleteButton);
            productItem =view.findViewById(R.id.productItem);
        }
    }


}
