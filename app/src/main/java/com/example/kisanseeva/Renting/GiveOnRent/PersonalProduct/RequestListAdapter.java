package com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.R;

import java.util.ArrayList;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.PersonList> {

    Context context;
    ArrayList<Person> requestArrayList;
    AcceptOrRejectApplication acceptOrRejectApplication;

    public RequestListAdapter(Context context, ArrayList<Person> requestList, AcceptOrRejectApplication acceptOrRejectApplication) {
        this.context = context;
        this.requestArrayList = requestList;
        this.acceptOrRejectApplication = acceptOrRejectApplication;
    }

    @NonNull
    @Override
    public PersonList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PersonList(LayoutInflater.from(context).inflate(R.layout.person_request_item, parent, false));
    }

    int currPos;
    @Override
    public void onBindViewHolder(@NonNull PersonList holder, int position) {
        Person curr = requestArrayList.get(position);
        holder.personName.setText(curr.getFirstName());
        Glide.with(context).load(curr.getProfileImg()).into(holder.personProfile);

        holder.acceptButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure?");
            builder.setMessage("Select an option");

            builder.setPositiveButton("Accept", (dialog, which) -> {
                acceptOrRejectApplication.acceptButtonListener(holder.getAdapterPosition());
                dialog.cancel();
            });
            builder.setNegativeButton("Reject",null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Log.v("testing","line 51");

        });
    }


    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    class PersonList extends RecyclerView.ViewHolder {
        ImageView personProfile;
        TextView personName;
        Button acceptButton;

        public PersonList(@NonNull View view) {
            super(view);
            personProfile = view.findViewById(R.id.person_img);
            personName = view.findViewById(R.id.person_name);
            acceptButton = view.findViewById(R.id.acceptButton);
        }
    }
}


