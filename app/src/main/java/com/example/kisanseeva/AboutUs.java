package com.example.kisanseeva;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {
    TextView contactNo, emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        contactNo = findViewById(R.id.contactNo);
        emailId = findViewById(R.id.emailId);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        String mailAddrs = "miniproject782@gmail.com";
        emailId.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + mailAddrs)); // only email apps should handle this
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        contactNo.setOnClickListener(view -> {
            String phoneno = "0123456789";
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + phoneno));
            startActivity(i);
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}