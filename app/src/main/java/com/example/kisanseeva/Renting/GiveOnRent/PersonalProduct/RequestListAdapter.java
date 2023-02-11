package com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct;

import android.content.Context;
import android.graphics.Color;
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

    ArrayList<String> applicationStatusList;

    public RequestListAdapter(Context context, ArrayList<Person> requestList, AcceptOrRejectApplication acceptOrRejectApplication, ArrayList<String> applicationStatusList) {
        this.context = context;
        this.requestArrayList = requestList;
        this.acceptOrRejectApplication = acceptOrRejectApplication;
        this.applicationStatusList = applicationStatusList;
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

        String condition = applicationStatusList.get(holder.getAdapterPosition());
        if (condition.equals("true")) {
            holder.acceptButton.setVisibility(View.GONE);
            holder.pendingApproval.setVisibility(View.VISIBLE);
        } else if (condition.equals("false")) {
            holder.acceptButton.setVisibility(View.GONE);
            holder.pendingApproval.setVisibility(View.VISIBLE);
            holder.pendingApproval.setText("Offer Declined");
            holder.pendingApproval.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.acceptButton.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setMessage("Select an option");

                builder.setPositiveButton("Accept", (dialog, which) -> {
                    acceptOrRejectApplication.acceptButtonListener(holder.getAdapterPosition());
                    holder.acceptButton.setEnabled(false);
                    dialog.cancel();
                });
                builder.setNegativeButton("Reject", (dialog, which) -> {
                    acceptOrRejectApplication.rejectButtonListener(holder.getAdapterPosition());
                    holder.acceptButton.setEnabled(false);
                    dialog.cancel();
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }

    }


    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    class PersonList extends RecyclerView.ViewHolder {
        ImageView personProfile;
        TextView personName, pendingApproval;
        Button acceptButton;

        public PersonList(@NonNull View view) {
            super(view);
            personProfile = view.findViewById(R.id.person_img);
            personName = view.findViewById(R.id.person_name);
            acceptButton = view.findViewById(R.id.acceptButton);
            pendingApproval = view.findViewById(R.id.pendingApproval);
        }
    }
}


