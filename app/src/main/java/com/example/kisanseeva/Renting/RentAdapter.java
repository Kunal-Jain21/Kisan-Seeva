package com.example.kisanseeva.Renting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kisanseeva.R;

import java.util.ArrayList;

public class RentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TakeOnRentModel> rentModelArrayList;
    public RentAdapter(ArrayList<TakeOnRentModel> rentModelArrayList, Context context) {
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
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.grid_member,parent,false);
        }
        ImageView imageView = view.findViewById(R.id.equipmentImg);
        TextView textView = view.findViewById(R.id.equipmentName);
        imageView.setImageResource(rentModelArrayList.get(position).getImdId());
        textView.setText(rentModelArrayList.get(position).getEquipment_name());
        return view;
    }
}


//public class RentAdapter extends GridView<RentAdapter.RentViewHolder> {
//    private ArrayList<TakeOnRentModel> rentModelArrayList;
//    private Context context;
//
//    public RentAdapter(ArrayList<TakeOnRentModel> rentModelArrayList, Context context) {
//        this.rentModelArrayList = rentModelArrayList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public RentAdapter.RentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.grid_member,parent,false);
//        return new RentAdapter.RentViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RentAdapter.RentViewHolder holder, int position) {
//        TakeOnRentModel rentModel = rentModelArrayList.get(position);
//        holder.equipmentName.setText(rentModel.getEquipment_name());
//        holder.equipmentImg.setImageResource(rentModel.getImdId());
//    }
//
//    @Override
//    public int getItemCount() {
//        return rentModelArrayList.size();
//    }
//
//    public class RentViewHolder extends RecyclerView.ViewHolder {
//        private ImageView equipmentImg;
//        private TextView equipmentName;
//        public RentViewHolder(@NonNull View itemView) {
//            super(itemView);
//            equipmentImg = itemView.findViewById(R.id.equipmentImg);
//            equipmentName = itemView.findViewById(R.id.equipmentName);
//        }
//    }
//}
