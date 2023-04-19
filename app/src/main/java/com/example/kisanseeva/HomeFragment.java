package com.example.kisanseeva;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kisanseeva.ml.LiteModelPlantDiseaseDefault1;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class HomeFragment extends Fragment {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    Button camera, gallery;
    ImageView imageView;
    TextView result;
    int imageSize = 224;
    String[] cameraPermissions;
    String storagePermissions[];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        camera = view.findViewById(R.id.button);
        gallery = view.findViewById(R.id.button2);
        result = view.findViewById(R.id.result);
        imageView = view.findViewById(R.id.imageView);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraPermission()) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestCameraPermission();
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkStoragePermission()) {
                    Log.v("testing", "inside if");
                    Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    Log.v("testing1", "inside else");
                    requestStoragePermission();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        Log.v("testing", "Line 104");
        ActivityCompat.requestPermissions(requireActivity(), storagePermissions, STORAGE_REQUEST_CODE);
        Log.v("testing", "Line 106");
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), cameraPermissions, CAMERA_REQUEST_CODE);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        Log.v("testing", "given both permission");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 3);
                    } else {
                        Toast.makeText(getActivity(), "Please enable camera & storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE: {
                Log.v("testing", "Line 135");
                if (grantResults.length > 0) {
                    Log.v("testing", "Line 139");
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(cameraIntent, 1);
                    } else {
                        Toast.makeText(getActivity(), "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            } else {
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SetTextI18n")
    private void classifyImage(Bitmap image) {
        try {
//            ImageClassifier model = ImageClassifier.newInstance(requireContext());
            LiteModelPlantDiseaseDefault1 model = LiteModelPlantDiseaseDefault1.newInstance(requireContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValue = new int[imageSize * imageSize];
            image.getPixels(intValue, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValue[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            LiteModelPlantDiseaseDefault1.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
            float[] confidence = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = Integer.MIN_VALUE;
            for (int i = 0; i < confidence.length; i++) {
                if (confidence[i] > maxConfidence) {
                    maxConfidence = confidence[i];
                    maxPos = i;
                }
            }
            Log.v("testing", maxPos + "" + Arrays.toString(confidence));
//            String[] classes = {"Leaf", "Other"};
//            String[] classes = {"aeroplane", "antelope", "badger", "bat", "bear", "bee", "beetle", "bison", "boar", "butterfly", "car", "cat", //12
//                    "caterpillar", "chimpanzee", "cockroach", "cow", "coyote", "crab", "crow", "deer", "dog", "dolphin", "donkey", "dragonfly", // 12
//                    "duck", "eagle", "elephant", "flamingo", "flower", "fly", "fox", "fruit", "goat", "goldfish", "goose", "gorilla", "grasshopper", //13
//                    "hamster", "hare", "hedgehog", "hippopotamus", "hornbill", "horse", "hummingbird", "hyena", "jellyfish", "kangaroo", "koala", // 11
//                    "ladybugs", "leaf", "leopard", "lion", "lizard", "lobster", "mosquito", "moth", "motorbike", "mouse", "octopus", "okapi", "orangutan", // 13
//                    "otter", "owl", "ox", "oyster", "panda", "parrot", "pelecaniformes", "penguin", "person", "pig", "pigeon", "porcupine", "possum",
//                    "raccoon", "rat", "reindeer", "rhinoceros", "sandpiper", "seahorse", "seal", "shark", "sheep", "snake", "sparrow", "squid", "squirrel",
//                    "starfish", "swan", "tiger", "turkey", "turtle", "whale", "wolf", "wombat", "woodpecker", "zebra"};

            String[] classes = {"Apple Scab", "Apple Black Rot", "Cedar Apple Rust", "Healthy Apple", "Healthy Blueberry",
                    "Cherry Powdery mildew", "Healthy Cherry", "Corn Gray leaf spot",
                    "Corn Common Rust", "Corn Northern Leaf Blight", "Healthy Corn", "Grape Black Rot", "Grape Black Measles",
                    "Grape Leaf blight", "Healthy Grape", "Orange Haunglongbing",
                    "Peach Bacterial Spot", "Peach Healthy", "Pepper Bacterial Spot", "Healthy Pepper", "Potato Early Blight",
                    "Healthy Potato", "Potato Late Blight", "Healthy Raspberry", "Healthy Soyabean", "Squash Powdery Mildew", "Strawberry Healthy", "Strawberry Leaf Scorch",
                    "Tomato Bacterial Spot", "Tomato Early Blight", "Tomato Healthy", "Tomato Late Blight", "Tomato Leaf Mold",
                    "Tomato Septoria Leaf Spot", "Tomato Spider Mites Two-spotted Spider Mite",
                    "Tomato Target Spot", "Tomato Mosaic Virus", "Tomato Yellow Leaf Curl Virus"};

            result.setText(" " + classes[maxPos] );
            Arrays.fill(confidence, 0);
//            Log.v("Prediction", String.valueOf(outputs));
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            //  TODO Handle the exception
        }
    }
}