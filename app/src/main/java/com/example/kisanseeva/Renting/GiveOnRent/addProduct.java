package com.example.kisanseeva.Renting.GiveOnRent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kisanseeva.R;
import com.example.kisanseeva.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.DocumentReference;

public class addProduct extends AppCompatActivity {

    private EditText machine_name,machine_desc,machine_price;
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
        machine_name = findViewById(R.id.machine_name);
        machine_desc = findViewById(R.id.machine_desc);
        machine_price = findViewById(R.id.machine_price);
        submit_btn = findViewById(R.id.submit_button);

        onImageButtonClicked();


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prod_name = machine_name.getText().toString();
                prod_desc = machine_desc.getText().toString();
                prod_price = machine_price.getText().toString();

                if (validateData(prod_name, prod_desc, prod_price)) {
                    productModel productModel =
                            new productModel("1", "aca", "product", 120, R.drawable.leaf_blower);
                    addProductToFirebase(productModel);
                }
                else {
                    return;
                }
            }
        });
    }

    boolean validateData(String prod_name, String prod_desc, String prod_price) {
        if (prod_name == null || prod_name.isEmpty()) {

        }
        return true;
    }

    private void addProductToFirebase(productModel productModel) {
        DocumentReference documentReference = Utility.getCollectionReferenceForProduct().document();
        documentReference.set(productModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Product add", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Error Product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onImageButtonClicked() {
        img_uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(addProduct.this)
                        //                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        //                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        //                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            };
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        prod_img.setImageURI(uri);
    }
}