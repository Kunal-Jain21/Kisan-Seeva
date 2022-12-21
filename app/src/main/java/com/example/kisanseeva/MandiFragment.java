package com.example.kisanseeva;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.Mandi.Crop;
import com.example.kisanseeva.Mandi.CropAdapter;
import com.example.kisanseeva.Mandi.RetroFitAPI;
import com.example.kisanseeva.Mandi.Settings;
import com.example.kisanseeva.Mandi.cropModel;

import androidx.annotation.NonNull;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MandiFragment extends Fragment {

    private RecyclerView recycle;
    ArrayList<Crop> cropArrayList;
    private CropAdapter cropAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mandi, container, false);

        recycle = view.findViewById(R.id.Recycler);
        cropArrayList = new ArrayList<>();

        cropAdapter = new CropAdapter(cropArrayList, getContext());
        recycle.setAdapter(cropAdapter);
        recycle.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        getCrop("All");
        cropAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new Settings()).addToBackStack("Mandi Page");
            fragmentTransaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterList(String s) {
        List<Crop> filteredList = new ArrayList<>();
        for (Crop crop : cropArrayList){
            if (crop.getState().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(crop);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(getContext(),"No data found",Toast.LENGTH_SHORT).show();
        }else{
            cropAdapter.setFilteredList(filteredList);
        }
    }

    public void getCrop(String cropName){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String preferenceString = sharedPrefs.getString("select_state", "All");

        cropArrayList.clear();
        String base_url = "https://data.gov.in/";
        String url = "";
        if (Objects.equals(preferenceString, "All")) {
            url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001888f0650bd234d6d721fe6a18a68ae33&format=json&limit=10000";
        }
        else {
            url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001888f0650bd234d6d721fe6a18a68ae33&format=json&limit=10000&filters[state]=" + preferenceString;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        RetroFitAPI retroFitAPI = retrofit.create(RetroFitAPI.class);
        Call<cropModel> call;
        call = retroFitAPI.getAllDetail(url);
        Log.v("Crop Failed", "Line 115");
        call.enqueue(new Callback<cropModel>() {
            @Override
            public void onResponse(Call<cropModel> call, Response<cropModel> response) {
                cropModel cropModel = response.body();
                ArrayList<Crop> crop = cropModel.getRecords();
                for (int i = 0; i < crop.size(); i++) {
                    cropArrayList.add(new Crop(crop.get(i).getState(),
                            crop.get(i).getMarket(), crop.get(i).getCommodity(),
                            crop.get(i).getArrival_date(), crop.get(i).getModal_price()));
                }
                cropAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<cropModel> call, Throwable t) {
                Toast.makeText(getContext(),"Fail to get crop",Toast.LENGTH_SHORT).show();
            }
        });
    }

}