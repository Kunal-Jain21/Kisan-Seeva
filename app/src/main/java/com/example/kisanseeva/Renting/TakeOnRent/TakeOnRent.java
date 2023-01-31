package com.example.kisanseeva.Renting.TakeOnRent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.R;

import java.util.ArrayList;

public class TakeOnRent extends Fragment {
    private Button takeOnRent;
    private GridView gridTools;
    private RecyclerView recyclerView;
    ArrayList<TakeOnRentModel> rentModelArrayList;
    private RentMenuAdapter rentMenuAdapter;

    public static TakeOnRent newInstance(String param1, String param2) {
        TakeOnRent fragment = new TakeOnRent();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_on_rent, container, false);

        ArrayList<TakeOnRentModel> rentModelArrayList = new ArrayList<TakeOnRentModel>();
        rentModelArrayList.add(new TakeOnRentModel("Hedge Trimmer", R.drawable.hedge));
        rentModelArrayList.add(new TakeOnRentModel("Lawn Mower", R.drawable.lawn_mover));
        rentModelArrayList.add(new TakeOnRentModel("Leaf Blower", R.drawable.leaf_blower));
        rentModelArrayList.add(new TakeOnRentModel("Hand Tools", R.drawable.hand_tools));
        rentModelArrayList.add(new TakeOnRentModel("Irrigation", R.drawable.irrigation));
        rentModelArrayList.add(new TakeOnRentModel("Pressure Washer", R.drawable.pressure_washer));
        rentModelArrayList.add(new TakeOnRentModel("Motor", R.drawable.motor));
        rentModelArrayList.add(new TakeOnRentModel("Inter-cultivator", R.drawable.ic_baseline_home_24));
        rentModelArrayList.add(new TakeOnRentModel("Engine", R.drawable.ic_baseline_home_24));
        rentModelArrayList.add(new TakeOnRentModel("Harvester", R.drawable.ic_baseline_home_24));
        rentModelArrayList.add(new TakeOnRentModel("Water pump", R.drawable.ic_baseline_home_24));
        rentModelArrayList.add(new TakeOnRentModel("Sprayer", R.drawable.ic_baseline_home_24));

        rentModelArrayList.add(new TakeOnRentModel("Seeder-Transplanter", R.drawable.ic_baseline_home_24));
        rentModelArrayList.add(new TakeOnRentModel("Brush cutter", R.drawable.ic_baseline_home_24));
        rentModelArrayList.add(new TakeOnRentModel("Chainsaw", R.drawable.ic_baseline_home_24));
        rentModelArrayList.add(new TakeOnRentModel("Dairy", R.drawable.ic_baseline_home_24));

        gridTools = view.findViewById(R.id.gridTools);
        gridTools.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(requireActivity(), RentedProductList.class);
            intent.putExtra("equipmentName", rentModelArrayList.get(i).getEquipment_name());
            startActivity(intent);
        });
        rentMenuAdapter = new RentMenuAdapter(rentModelArrayList, getContext());
        gridTools.setAdapter(rentMenuAdapter);
        rentMenuAdapter.notifyDataSetChanged();
        return view;

    }
}