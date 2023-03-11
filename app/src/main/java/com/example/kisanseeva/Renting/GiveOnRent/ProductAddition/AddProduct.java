package com.example.kisanseeva.Renting.GiveOnRent.ProductAddition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kisanseeva.R;
import com.example.kisanseeva.Renting.GiveOnRent.ProductModel;
import com.example.kisanseeva.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AddProduct extends AppCompatActivity {

    private EditText nameEditText, descEditText, priceEditText;
    private ImageView prod_img;
    ImageButton img_uploader;
    String selectedCategory;
    Uri uri;
    Button submit_btn;
    String prod_name, prod_desc, prod_price;
    Spinner category_spinner;

    ProgressBar progressBarAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prod_img = findViewById(R.id.prod_img);
        img_uploader = findViewById(R.id.img_but);
        nameEditText = findViewById(R.id.machine_name);
        descEditText = findViewById(R.id.machine_desc);
        priceEditText = findViewById(R.id.machine_price);
        submit_btn = findViewById(R.id.submit_button);
        category_spinner = findViewById(R.id.category_spinner);
        progressBarAddProduct = findViewById(R.id.progressBarAddProduct);

        descEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(100)});

        String[] category = getResources().getStringArray(R.array.category);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(categoryAdapter);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCategory = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        onUploadImageButtonClicked();
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_btn.setVisibility(View.GONE);
                progressBarAddProduct.setVisibility(View.VISIBLE);
                addProduct();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    void addProduct() {
        prod_name = nameEditText.getText().toString().trim();
        prod_desc = descEditText.getText().toString().trim();
        prod_price = priceEditText.getText().toString().trim();

        if (validateData(prod_name, prod_desc, prod_price)) {
            // If entered data is correct
            ProductModel currProduct = new ProductModel();
            currProduct.setProd_name(prod_name);
            currProduct.setProd_desc(prod_desc);
            currProduct.setProd_price(Integer.parseInt(prod_price));
            currProduct.setCategory(selectedCategory);
            addProductToFirebase(currProduct);
        }
    }

    boolean validateData(String prod_name, String prod_desc, String prod_price) {
        if (prod_name == null || prod_name.isEmpty()) {
            nameEditText.setError("Please give Name to Product");
            progressBarAddProduct.setVisibility(View.INVISIBLE);
            submit_btn.setVisibility(View.VISIBLE);
            return false;
        }

        if (prod_desc == null || prod_desc.isEmpty()) {
            descEditText.setError("Please add description");
            progressBarAddProduct.setVisibility(View.INVISIBLE);
            submit_btn.setVisibility(View.VISIBLE);
            return false;
        }

        if (uri == null) {
            Toast.makeText(this, "Upload Equipment Image", Toast.LENGTH_SHORT).show();
            progressBarAddProduct.setVisibility(View.INVISIBLE);
            submit_btn.setVisibility(View.VISIBLE);
            return false;
        }

        if (prod_price == null || prod_price.isEmpty()) {
            priceEditText.setError("Please enter price");
            progressBarAddProduct.setVisibility(View.INVISIBLE);
            submit_btn.setVisibility(View.VISIBLE);
            return false;
        }
        if (selectedCategory.equals("Select Category")) {
            ((TextView) category_spinner.getSelectedView()).setError("Select a Category");
            progressBarAddProduct.setVisibility(View.INVISIBLE);
            submit_btn.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void addProductToFirebase(ProductModel currProduct) {
        StorageReference storageReference = Utility.getStorageReferenceForProductImage();
        // adding image to storage
        storageReference.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        // adding product to product Table
                        DocumentReference rentedProductDocument = Utility.getCollectionReferenceForRentedProduct().document();
                        DocumentReference productDocument = Utility.getDocumentReferenceOfUser().collection("my_product").document();
                        currProduct.setPersonal_prod_id(productDocument.getId());
                        currProduct.setGiver_id(Utility.getCurrentUser().getUid());
                        currProduct.setProd_img(uri1.toString());
                        currProduct.setProd_id(rentedProductDocument.getId());
                        // adding product id is personal list
                        rentedProductDocument.set(currProduct).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                productDocument.set(new HashMap<String, String>() {{
                                    put("prodId", rentedProductDocument.getId());
                                }}).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Error in adding product to list", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(this, "Error in adding product", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressBarAddProduct.setVisibility(View.INVISIBLE);
                    submit_btn.setVisibility(View.VISIBLE);
                }); ;
    }

    private void onUploadImageButtonClicked() {
        img_uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddProduct.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(10);
            }

            ;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            uri = data.getData();
            prod_img.setImageURI(uri);
        }

    }
}