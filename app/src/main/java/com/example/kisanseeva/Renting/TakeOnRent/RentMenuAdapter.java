package com.example.kisanseeva.Renting.TakeOnRent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kisanseeva.R;

import java.util.ArrayList;

public class RentMenuAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TakeOnRentModel> rentModelArrayList;


    public RentMenuAdapter(ArrayList<TakeOnRentModel> rentModelArrayList, Context context) {
        this.context = context;
        this.rentModelArrayList = rentModelArrayList;
    }

    @Override
    public int getCount() {
        return rentModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return rentModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_member, parent, false);
        }
        ImageView imageView = view.findViewById(R.id.equipmentImg);
        TextView textView = view.findViewById(R.id.equipmentName);
        imageView.setImageResource(rentModelArrayList.get(position).getImdId());
        textView.setText(rentModelArrayList.get(position).getEquipment_name());
        LinearLayout product_item = view.findViewById(R.id.product_item);
        return view;
    }
}
