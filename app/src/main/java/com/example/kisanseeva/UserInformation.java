package com.example.kisanseeva;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kisanseeva.Renting.GiveOnRent.PersonalProduct.Person;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class UserInformation extends AppCompatActivity {

    ShapeableImageView profilePic;
    TextInputLayout firstName, lastname, emailAddress, postalAddress, pinCode;
    RadioGroup genderRadioGrp;
    RadioButton femaleRadioButton;
    Spinner stateSpinner, citySpinner;
    MaterialButton submitBtn;
    String personFirstName, personLastName, personEmail,
            personAddress, personPinCode, personGender = "Select", selectedState, selectedCity;
    ProgressBar progressBar;
    ImageButton uploadImage;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        profilePic = findViewById(R.id.profileImg);
        uploadImage = findViewById(R.id.uploadImage);
        firstName = findViewById(R.id.firstName);
        lastname = findViewById(R.id.lastname);
        emailAddress = findViewById(R.id.emailAddress);
        postalAddress = findViewById(R.id.postalAddress);
        pinCode = findViewById(R.id.pinCode);
        genderRadioGrp = findViewById(R.id.genderRadioGrp);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        stateSpinner = findViewById(R.id.stateSpinner);
        citySpinner = findViewById(R.id.citySpinner);
        submitBtn = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progressBar);

        setStateSpinner();
        setCitySpinner();
        uploadImage.setOnClickListener(view -> {
            ImagePicker.with(UserInformation.this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
//                                            .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start(10);
        });

        genderRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
                switch (checkedButtonId) {
                    case R.id.maleRadioButton: {
                        personGender = "Male";
                        break;
                    }
                    case R.id.femaleRadioButton: {
                        personGender = "Female";
                    }
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            uri = data.getData();
            profilePic.setImageURI(uri);
        }
    }

    private void setStateSpinner() {
        ArrayList<String> stateArray = new ArrayList<String>(
                Arrays.asList("Select State", "Maharashtra")
        );

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stateArray);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                selectedState = adapterView.getItemAtPosition(index).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCitySpinner() {
        ArrayList<String> cityArray = new ArrayList<String>(
                Arrays.asList("Select City", "Mumbai")
        );

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityArray);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                selectedCity = adapterView.getItemAtPosition(index).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getDataFromField() {
        personFirstName = firstName.getEditText().getText().toString().trim();
        personLastName = lastname.getEditText().getText().toString().trim();
        personEmail = emailAddress.getEditText().getText().toString().trim();
        personAddress = postalAddress.getEditText().getText().toString().trim();
        personPinCode = pinCode.getEditText().getText().toString().trim();


    }

    private boolean verifyData() {
        String pinCoderRegex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        Pattern p = Pattern.compile(pinCoderRegex);


        if (personFirstName == null || personFirstName.isEmpty()) {
            firstName.setError("Enter First Name");
            return false;
        } else {
            firstName.setError(null);
        }

        if (personLastName == null || personLastName.isEmpty()) {
            lastname.setError("Enter proper Surname");
            return false;
        } else {
            lastname.setError(null);
        }

        if (personGender.equals("Select")) {
            femaleRadioButton.setError("Select Gender");
            return false;
        } else {
            femaleRadioButton.setError(null);
        }

        if (personEmail == null || personEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(personEmail).matches()) {
            emailAddress.setError("Enter proper Email Address");
            return false;
        } else {
            emailAddress.setError(null);
        }

        if (personAddress == null || personAddress.isEmpty()) {
            postalAddress.setError("Enter proper Address");
            return false;
        } else {
            postalAddress.setError(null);
        }

        if (selectedState.equals("Select State")) {
            ((TextView) stateSpinner.getSelectedView()).setError("Select State");
            return false;
        } else {
            ((TextView) stateSpinner.getSelectedView()).setError(null);
        }

        if (selectedCity.equals("Select City")) {
            ((TextView) citySpinner.getSelectedView()).setError("Select City");
            return false;
        } else {
            ((TextView) citySpinner.getSelectedView()).setError(null);
        }

        if (personPinCode == null || personPinCode.isEmpty() || !p.matcher(personPinCode).matches()) {
            pinCode.setError("Enter 6 digit pin code");
            return false;
        } else {
            pinCode.setError(null);
        }

        return true;
    }

    private void saveData() {
        progressBar.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);
        getDataFromField();
        if (verifyData()) {
            Person currUser = new Person();
            currUser.setFirstName(personFirstName);
            currUser.setLastName(personLastName);
            currUser.setAddress(personAddress);
            currUser.setEmail(personEmail);
            currUser.setPinCode(personPinCode);
            currUser.setGender(personGender);
            currUser.setState(selectedState);
            currUser.setCity(selectedCity);
            if (uri == null) {
                currUser.setProfileImg("https://firebasestorage.googleapis.com/v0/b/kisan-seeva-6c8fd.appspot.com/o/profileImages%2Fuser.png?alt=media&token=131c948a-075e-4fd6-8366-76218dfed62a");
            } else {
                uploadImage(currUser);
            }
            addProductToFirebase(currUser);
        } else {
            progressBar.setVisibility(View.GONE);
            submitBtn.setVisibility(View.VISIBLE);
        }
    }

    private void uploadImage(Person currUser) {
        StorageReference storageReference = Utility.getStorageReferenceForProfileImage();
        UploadTask uploadTask = storageReference.putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnSuccessListener(imageUrl -> {
                currUser.setProfileImg(imageUrl.toString());
            });
        });
    }

    private void addProductToFirebase(Person currUser) {
        Utility.getDocumentReferenceOfUser().set(currUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Person data added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserInformation.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                progressBar.setVisibility(View.GONE);
                submitBtn.setVisibility(View.VISIBLE);
            }
        });
    }
}