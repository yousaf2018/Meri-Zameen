package com.example.meri_zameen;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ImageAnalysis extends AppCompatActivity {
    private static final String TAG = "Check";
    ImageView imageView;
    Button select, prediction, report;
    TextView pValue, pHValue, omValue, ecValue;
    String imageBase64 = "";
    CardView cardView;
    ProgressBar progBar;
    String url = "https://soil-analysis.herokuapp.com/predict";
    DecimalFormat df = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_analysis);
        getSupportActionBar().hide();
        imageView = findViewById(R.id.image);
        select = findViewById(R.id.selectButton);
        prediction = findViewById(R.id.Prediction);
        pValue = findViewById(R.id.PValue);
        pHValue = findViewById(R.id.pHValue);
        omValue = findViewById(R.id.OMValue);
        ecValue = findViewById(R.id.ECValue);
        progBar = findViewById(R.id.progressBar);
        cardView = findViewById(R.id.predictionCard);
        report = findViewById(R.id.generateReport);
        report.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        progBar.setVisibility(View.GONE);
        //When user clicks on image
        imageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick == true){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if(!checkCameraPermission()){
                            requestCameraPermission();
                        }
                        else{
                            PickImage();
                        }
                    }
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if(!checkStoragePermission()){
                            requestStoragePermission();
                        }
                        else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                PickImage();
                            }
                        }
                    }

                }
            }
        });
        //When user clicks on select button
        select.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick == true){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if(!checkCameraPermission()){
                            requestCameraPermission();
                        }
                        else{
                            PickImage();
                        }
                    }
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if(!checkStoragePermission()){
                            requestStoragePermission();
                        }
                        else{
                            PickImage();
                        }
                    }

                }
            }
        });
        //When user clicks on prediction button
        prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progBar.setVisibility(View.VISIBLE);
                //API call
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    double P = (double) jsonObject.get("P");
                                    double pH = (double) jsonObject.get("pH");
                                    double OM = (double) jsonObject.get("OM");
                                    double EC = (double) jsonObject.get("EC");

                                    Log.i(TAG,"Pred-->"+ P);
                                    pValue.setText(df.format(P)+"");
                                    omValue.setText(df.format(OM)+"");
                                    ecValue.setText(df.format(EC)+"");
                                    pHValue.setText(df.format(pH)+"");
                                    report.setVisibility(View.VISIBLE);
                                    cardView.setVisibility(View.VISIBLE);
                                    progBar.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    progBar.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }


                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(ImageAnalysis.this, error.getMessage(), Toast.LENGTH_SHORT);
                                progBar.setVisibility(View.GONE);

                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("inputImage", imageBase64);
                        return param;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ImageAnalysis.this);
                queue.add(stringRequest);
            }
        });
    }

    private void PickImage() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream stream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    //Encoding image to base 64
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                    byte[] bytes = stream1.toByteArray();
                    imageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                    Toast.makeText(ImageAnalysis.this, imageBase64, Toast.LENGTH_SHORT);
                    //Decoding image to bitmap
                    byte[] bytes1 = Base64.decode(imageBase64, Base64.DEFAULT);
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
                    imageView.setImageBitmap(bitmap1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}