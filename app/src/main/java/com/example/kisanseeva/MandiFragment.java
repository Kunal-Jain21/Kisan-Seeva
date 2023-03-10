package com.example.kisanseeva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kisanseeva.Mandi.Crop;
import com.example.kisanseeva.Mandi.CropAdapter;
import com.example.kisanseeva.Mandi.CropModel;
import com.example.kisanseeva.Mandi.RetroFitAPI;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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

    String selectedState = "All";
    int selectedStatePosition = 0;

    ProgressBar mandiProgressBar;

    TextView failureText;


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
        mandiProgressBar = view.findViewById(R.id.mandiProgressBar);
        failureText = view.findViewById(R.id.failureText);

        cropArrayList = new ArrayList<>();

        cropAdapter = new CropAdapter(cropArrayList, getContext());
        recycle.setAdapter(cropAdapter);
        recycle.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        getCrop();
        cropAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
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
        if (id == R.id.action_setting) {
            View view = LayoutInflater.from(requireActivity()).inflate(R.layout.mandi_bottom_sheet_layout, null);
            Spinner stateSpinner = view.findViewById(R.id.newStateSpinner);
            Button applyButton = view.findViewById(R.id.applyBtn);

            String[] arr = getResources().getStringArray(R.array.india_states);
            ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, arr);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setSelection(selectedStatePosition);

            stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedState = arr[i];
                    selectedStatePosition = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();

            applyButton.setOnClickListener(view1 -> {
                bottomSheetDialog.dismiss();
                getCrop();
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterList(String s) {
        List<Crop> filteredList = new ArrayList<>();
        for (Crop crop : cropArrayList) {
            if (crop.getState().toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(crop);
            }
        }
        if (filteredList.isEmpty()) {
            failureText.setText("No data found");
            failureText.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }else {
            failureText.setVisibility(View.INVISIBLE);
        }
        cropAdapter.setFilteredList(filteredList);
    }

    public void getCrop() {
        mandiProgressBar.setVisibility(View.VISIBLE);
        cropArrayList.clear();
        String base_url = "https://data.gov.in/";
        String url = "";
        if (Objects.equals(selectedState, "All")) {
            url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001888f0650bd234d6d721fe6a18a68ae33&format=json&limit=10000";
        } else {
            url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001888f0650bd234d6d721fe6a18a68ae33&format=json&limit=10000&filters[state]=" + selectedState;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        RetroFitAPI retroFitAPI = retrofit.create(RetroFitAPI.class);
        Call<CropModel> call;
        call = retroFitAPI.getAllDetail(url);
        call.enqueue(new Callback<CropModel>() {
            @Override
            public void onResponse(Call<CropModel> call, Response<CropModel> response) {
                CropModel cropModel = response.body();
                ArrayList<Crop> crop = cropModel.getRecords();
                if (crop.size() == 0) {
                    mandiProgressBar.setVisibility(View.INVISIBLE);
                    failureText.setVisibility(View.VISIBLE);
                } else {
                    failureText.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < crop.size(); i++) {
                        cropArrayList.add(new Crop(crop.get(i).getState(),
                                crop.get(i).getMarket(), crop.get(i).getCommodity(),
                                crop.get(i).getArrival_date(), crop.get(i).getModal_price()));
                    }
                    mandiProgressBar.setVisibility(View.INVISIBLE);
                }

                cropAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CropModel> call, Throwable t) {
                mandiProgressBar.setVisibility(View.INVISIBLE);
                failureText.setText("Error while fetching data");
                failureText.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(), "Fail to get crop", Toast.LENGTH_SHORT).show();
            }
        });
    }

}