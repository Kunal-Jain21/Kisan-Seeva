package com.example.kisanseeva.Renting.GiveOnRent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kisanseeva.R;
import com.example.kisanseeva.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

public class addProduct extends AppCompatActivity {

    private EditText nameEditText,descEditText,priceEditText;
    private ImageView prod_img;
    ImageButton img_uploader;
    String path;
    Uri uri;
    Button submit_btn;
    String prod_name, prod_desc, prod_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        prod_img = findViewById(R.id.prod_img);
        img_uploader = findViewById(R.id.img_but);
        nameEditText = findViewById(R.id.machine_name);
        descEditText = findViewById(R.id.machine_desc);
        priceEditText = findViewById(R.id.machine_price);
        submit_btn = findViewById(R.id.submit_button);

        onUploadImageButtonClicked();
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
    }

    boolean validateData(String prod_name, String prod_desc, String prod_price) {
        if (prod_name == null || prod_name.isEmpty()) {
            nameEditText.setError("Please give Name to Product");
            return false;
        }
        if (prod_price == null || prod_price.isEmpty()) {
            nameEditText.setError("Please enter price");
            return false;
        }
        return true;
    }

    void addProduct() {
        prod_name = nameEditText.getText().toString();
        prod_desc = descEditText.getText().toString();
        prod_price = priceEditText.getText().toString();

        if (validateData(prod_name, prod_desc, prod_price)) {
            // If entered data is correct
            productModel productModel = new productModel("1", prod_name, "product", Integer.parseInt(prod_price), R.drawable.leaf_blower);
            addProductToFirebase(productModel);
        }
    }

    private void addProductToFirebase(productModel productModel) {
        StorageReference storageReference = Utility.getStorageReferenceForImage();

        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                DocumentReference documentReference = Utility.getCollectionReferenceForProduct().document();
//                productModel.setProd_img(uri1);
                productModel.setProd_id(documentReference.getId());
                    documentReference.set(productModel).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(this, "Error in adding product", Toast.LENGTH_SHORT).show();
                        }
                    });
            });
        });
    }

    private void onUploadImageButtonClicked() {
        img_uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(addProduct.this)
                        //                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        //                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        //                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(10);
            };
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            uri = data.getData();
            prod_img.setImageURI(uri);
            Log.v("checking", "Line 119");
        }

    }
}