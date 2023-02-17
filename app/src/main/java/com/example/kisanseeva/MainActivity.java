package com.example.kisanseeva;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kisanseeva.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private int selectedMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.v("testing", selectedMenu + "");

        binding.bottomNav.setOnItemSelectedListener(Item -> {

            switch (Item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    selectedMenu = R.id.home;
                    break;
                case R.id.mandi:
                    replaceFragment(new MandiFragment());
                    selectedMenu = R.id.mandi;
                    break;
                case R.id.renting:
                    selectedMenu = R.id.renting;
                    replaceFragment(new RentingFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    selectedMenu = R.id.profile;
                    break;

            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}