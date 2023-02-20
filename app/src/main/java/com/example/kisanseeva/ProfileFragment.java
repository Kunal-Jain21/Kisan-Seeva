package com.example.kisanseeva;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct.Person;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    MaterialButton editProfile;
    Person editPerson;
    CircleImageView profileImg;
    TextView nameOfUser;
    Button requestedProduct,logoutBtn;
    ProgressBar progressBar, progressBarProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBarProfile.setVisibility(View.VISIBLE);
        Utility.getDocumentReferenceOfUser(Utility.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            editPerson = documentSnapshot.toObject(Person.class);
            String name = editPerson.getFirstName()+" "+editPerson.getLastName();
            nameOfUser.setText(name);
            Uri uri2 = Uri.parse(editPerson.getProfileImg());
            Glide.with(this).load(uri2).into(profileImg);
        });
        progressBarProfile.setVisibility(View.GONE);
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        requestedProduct = view.findViewById(R.id.requestedProduct);
        progressBar = view.findViewById(R.id.progressBar);
        editProfile = view.findViewById(R.id.editProfile);
        profileImg = view.findViewById(R.id.profileImg);
        nameOfUser = view.findViewById(R.id.nameOfUser);
        progressBarProfile = view.findViewById(R.id.progressBarProfile);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        requestedProduct.setOnClickListener(view1 -> {
            startActivity(new Intent(requireActivity(), RequestedProductActivity.class));
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(requireActivity(), SendOTP.class));
                requireActivity().finish();
            }
        });

        Utility.getDocumentReferenceOfUser(Utility.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            editPerson = documentSnapshot.toObject(Person.class);
        });

        editProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireActivity(), UserInformation.class);
            intent.putExtra("edit",true);
            startActivity(intent);
        });
        return view;
    }
}