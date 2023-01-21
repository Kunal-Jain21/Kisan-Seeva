package com.example.kisanseeva;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kisanseeva.Renting.GiveOnRent.GiveOnRent;
import com.example.kisanseeva.Renting.TakeOnRent.TakeOnRent;

public class RentingFragment extends Fragment {
    private RadioButton radio0, radio1;
    private RadioGroup radioGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFragement(new TakeOnRent());

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_renting, container, false);
        radio0 = view.findViewById(R.id.radio0); // take on rent
        radio1 = view.findViewById(R.id.radio1); // give on rent

        radioGroup = view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
                switch (checkedButtonId) {
                    case R.id.radio0:
                        loadFragement(new TakeOnRent());
                        break;
                    case R.id.radio1:
                        loadFragement(new GiveOnRent());
                        break;
                }
            }
        });


        return view;
    }

    private void loadFragement(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.rentingFrame, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}