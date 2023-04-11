package com.example.kisanseeva;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct.Person;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    MaterialButton editProfile;
    Person editPerson;
    CircleImageView profileImg;
    TextView nameOfUser;
    Button requestedProduct,logoutBtn,aboutUs;
    ProgressBar progressBar, progressBarProfile;

    @Override
    public void onStart() {
        super.onStart();
        progressBarProfile.setVisibility(View.VISIBLE);
        Utility.getDocumentReferenceOfUser(Utility.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            editPerson = documentSnapshot.toObject(Person.class);
            String name = editPerson.getFirstName() + " " + editPerson.getLastName();
            nameOfUser.setText(name);
            Activity activity = getActivity();
            if (isAdded() && activity != null) {
                try {
                    Glide.with(requireActivity()).load(editPerson.getProfileImg()).into(profileImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        progressBarProfile.setVisibility(View.GONE);
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
        aboutUs = view.findViewById(R.id.aboutUs);

        requestedProduct.setOnClickListener(view1 -> {
            startActivity(new Intent(requireActivity(), RequestedProductActivity.class));
        });

        aboutUs.setOnClickListener(view1 -> {
            startActivity(new Intent(requireActivity(), AboutUs.class));
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
            Log.v("testing", "Kunal Jain");
            Intent intent = new Intent(requireActivity(), UserInformation.class);
            intent.putExtra("edit", true);
            requireActivity().startActivity(intent);
        });
        return view;
    }
}