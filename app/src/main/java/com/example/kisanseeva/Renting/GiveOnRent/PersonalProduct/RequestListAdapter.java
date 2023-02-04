package com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kisanseeva.R;

import java.util.ArrayList;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.PersonList> {

    Context context;
    ArrayList<Person> requestArrayList;

    public RequestListAdapter(Context context, ArrayList<Person> requestList) {
        this.context = context;
        this.requestArrayList = requestList;
    }

    @NonNull
    @Override
    public PersonList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PersonList(LayoutInflater.from(context).inflate(R.layout.person_request_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PersonList holder, int position) {
        Person curr = requestArrayList.get(position);
        holder.personName.setText(curr.getFirstName());
//        Glide.with(context).load(curr.getImage()).into(holder.personProfile);
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    class PersonList extends RecyclerView.ViewHolder {
        ImageView personProfile;
        TextView personName;
        public PersonList(@NonNull View view) {
            super(view);
            personProfile = view.findViewById(R.id.person_img);
            personName = view.findViewById(R.id.person_name);
        }
    }
}
