package com.example.kisanseeva.Mandi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.R;

import java.util.ArrayList;
import java.util.List;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {

    private ArrayList<Crop> cropArrayList;
    private Context context;

    public CropAdapter(ArrayList<Crop> cropArrayList, Context context) {
        this.cropArrayList = cropArrayList;
        this.context = context;
    }

    public void setFilteredList(List<Crop> filteredList){
        this.cropArrayList = (ArrayList<Crop>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_style, parent, false);
        return new CropAdapter.CropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop = cropArrayList.get(position);
        Log.v("adapter", crop.getCommodity());
        holder.cropName.setText(crop.getCommodity());
        holder.price.setText(crop.getModal_price());
        holder.state.setText(crop.getState());
        holder.date.setText(crop.getArrival_date());
    }

    @Override
    public int getItemCount() {
        return cropArrayList.size();
    }

    public class CropViewHolder extends RecyclerView.ViewHolder {
        private TextView cropName, price, state, date;
        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.Vegetable);
            price = itemView.findViewById(R.id.price);
            state = itemView.findViewById(R.id.State);
            date = itemView.findViewById(R.id.date);
        }
    }
}
