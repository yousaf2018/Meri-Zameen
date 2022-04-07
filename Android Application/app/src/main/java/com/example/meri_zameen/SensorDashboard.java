package com.example.meri_zameen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorDashboard extends AppCompatActivity {
    public static final String SHRED_PREF = "sharedPrefs";
    public static final String Save_Lang= "Lang";
    String lang, P, pH, OM, EC, N, H, T;
    Button connect;
    CardView cardView;
    EditText sensorId;
    ProgressBar progressBar;
    TextView checkConnection, Phosphorous, PH, Potassium, ElectricalCond, Nitrogen, Humidity, Temperature;
    DatabaseReference databaseReference;
    static String connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_dashboard);
        getSupportActionBar().hide();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHRED_PREF, getApplicationContext().MODE_PRIVATE);
        lang = pref.getString(Save_Lang, "Lang");
        setLanguage(lang);
        sensorId = findViewById(R.id.sensorID);
        connect = findViewById(R.id.btnCon);
        cardView = findViewById(R.id.cardCon);
        progressBar = findViewById(R.id.progCon);
        progressBar.setVisibility(View.GONE);
        checkConnection = findViewById(R.id.ConStat);
        Phosphorous = findViewById(R.id.pTab);
        ElectricalCond = findViewById(R.id.ecTab);
        Nitrogen = findViewById(R.id.nTab);
        PH = findViewById(R.id.phTab);
        Potassium = findViewById(R.id.kTab);
        Humidity = findViewById(R.id.hTab);
        Temperature = findViewById(R.id.tTab);
        //When user clicks on connect button
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = sensorId.getText().toString();
                if (id.isEmpty()){
                    Toast.makeText(SensorDashboard.this, "Kindly enter sensor id", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    //Getting database reference
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(id);
                    // Read from the database
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            dataHolder value = dataSnapshot.getValue(dataHolder.class);
                            if (value.getFlag().toString() == "true"){
//                                Toast.makeText(SensorDashboard.this, "Sensor is connected successfully", Toast.LENGTH_SHORT).show();
                                cardView.setBackgroundColor(Color.parseColor("#117c03"));
                                checkConnection.setText(R.string.Connected);
                                PH.setText(value.getpH().toString());
                                Nitrogen.setText(value.getN().toString());
                                Phosphorous.setText(value.getP().toString());
                                Potassium.setText(value.getK().toString());
                                Humidity.setText(value.getHumidity().toString());
                                Temperature.setText(value.getTemperature().toString());
                                ElectricalCond.setText(value.getEC().toString());
                                connection = value.getFlag().toString();
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(SensorDashboard.this, "Sensor is not connected", Toast.LENGTH_SHORT).show();
                                cardView.setBackgroundColor(Color.parseColor("#EC0707"));
                                checkConnection.setText(R.string.NotConnected);
                                PH.setText("0.0");
                                Nitrogen.setText("0.0");
                                Phosphorous.setText("0.0");
                                Potassium.setText("0.0");
                                Humidity.setText("0.0");
                                Temperature.setText("0.0");
                                ElectricalCond.setText("0.0");
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
//                            Log.w(TAG, "Failed to read value.", error.toException());
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }
            }
        });
//        //If sensor is connected to fetch latest values
//        if(connection == "true"){
//            while(connection == "true"){
//                //Getting database reference
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("Sensor01");
//                // Read from the database
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        dataHolder value = dataSnapshot.getValue(dataHolder.class);
//                        if (value.getFlag().toString() == "true"){
//                            Toast.makeText(SensorDashboard.this, "Sensor is connected successfully", Toast.LENGTH_SHORT).show();
//                            cardView.setBackgroundColor(Color.parseColor("#117c03"));
//                            checkConnection.setText(R.string.Connected);
//                            PH.setText(value.getpH().toString());
//                            Nitrogen.setText(value.getN().toString());
//                            Phosphorous.setText(value.getP().toString());
//                            Potassium.setText(value.getK().toString());
//                            Humidity.setText(value.getHumidity().toString());
//                            Temperature.setText(value.getTemperature().toString());
//                            ElectricalCond.setText(value.getEC().toString());
//                            connection = value.getFlag().toString();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                        else{
//                            Toast.makeText(SensorDashboard.this, "Sensor is not connected", Toast.LENGTH_SHORT).show();
//                            cardView.setBackgroundColor(Color.parseColor("#EC0707"));
//                            checkConnection.setText(R.string.NotConnected);
//                            PH.setText("0.0");
//                            Nitrogen.setText("0.0");
//                            Phosphorous.setText("0.0");
//                            Potassium.setText("0.0");
//                            Humidity.setText("0.0");
//                            Temperature.setText("0.0");
//                            connection = "false";
//                            ElectricalCond.setText("0.0");
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
////                            Log.w(TAG, "Failed to read value.", error.toException());
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//            }
//        }
    }

    //Setting the language
    private void setLanguage(String lang) {
        LanguageManager languageManager=new LanguageManager(this);
        if (lang.equals("Urdu")){
            languageManager.updateResourse("ur");
            // recreate();
        }
        else {
            languageManager.updateResourse("en");
            // recreate();
        }
    }
}