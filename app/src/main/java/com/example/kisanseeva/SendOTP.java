package com.example.kisanseeva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        getSupportActionBar().hide();

        final EditText inputMobile = findViewById(R.id.inputMobile);
        Button buttonGetOTP = findViewById(R.id.buttonGetOTP);

        final ProgressBar progressBar = findViewById(R.id.progressPar);

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNo = inputMobile.getText().toString().trim();
                if (mobileNo.length() != 10){
                    Toast.makeText(SendOTP.this,"Enter Mobile",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonGetOTP.setVisibility(View.GONE);


                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+mobileNo,
                        60,
                        TimeUnit.SECONDS,
                        SendOTP.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.VISIBLE);
                                Toast.makeText(SendOTP.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(),VerifyOTP.class);
                                intent.putExtra("mobile",mobileNo);
                                intent.putExtra("verificationId",verificationId);
                                startActivity(intent);
                            }

                        }
                );
            }
        });
    }
}