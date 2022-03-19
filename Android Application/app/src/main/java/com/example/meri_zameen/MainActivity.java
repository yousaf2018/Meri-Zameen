package com.example.meri_zameen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText inputValue;
    Button predict;
    TextView prediction;
    String url = "https://soil-nutrients-predictor.herokuapp.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Started work
        inputValue = findViewById(R.id.inputValue);
        predict = findViewById(R.id.predict);
        prediction = findViewById(R.id.prediction);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //API call
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    double data = (double) jsonObject.get("Prediction");
                                    prediction.setText(data+"");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT);

                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("inputValue", inputValue.getText().toString());
                            return param;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(stringRequest);
            }
        });
    }
}