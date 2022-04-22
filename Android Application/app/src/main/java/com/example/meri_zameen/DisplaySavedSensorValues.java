package com.example.meri_zameen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.number.NumberFormatter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplaySavedSensorValues extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    String lang;
    Button home;
    ArrayList<SavedValuesModel> list;
    ImageButton imageButton;
    EditText searchID;
    public static final String SHRED_PREF = "sharedPrefs";
    public static final String Save_Lang= "Lang";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_saved_values);
        Bundle bundle = getIntent().getExtras();
        String sampleID= bundle.getString("sensorID");
        Log.i("check", sampleID);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        home = findViewById(R.id.Home);
        searchID = (EditText) findViewById(R.id.searchID);
        imageButton = findViewById(R.id.imageButton);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHRED_PREF, getApplicationContext().MODE_PRIVATE);
        lang = pref.getString(Save_Lang, "Lang");
        // setLanguage(lang);
        LanguageManager languageManager=new LanguageManager(this);
        if (lang.equals("Urdu")){
            languageManager.updateResourse("ur");
            // recreate();
        }
        else {
            languageManager.updateResourse("en");
            // recreate();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new Adapter(list, this);
        recyclerView.setAdapter(adapter);
        //Setting the defualt values on card
        SavedValuesModel model = new SavedValuesModel("0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "Loading data from database wait", "123xxxx");
        list.add(model);
        adapter.notifyDataSetChanged();
        getSupportActionBar().hide();
        loadData();
        //When user clicks on search button
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = searchID.getText().toString();
                if (check.isEmpty()){
                    Toast.makeText(DisplaySavedSensorValues.this, "Kindly enter sample id for searching", Toast.LENGTH_SHORT).show();
                    loadData();
                }
                else{

                    //Getting values from database
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference().child("SavedValues");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                     Toast.makeText(DisplaySavedSensorValues.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
//                     Log.i("check3", dataSnapshot.toString());
                                SavedValuesModel model = dataSnapshot.getValue(SavedValuesModel.class);
                                if(model.getSampleID().toString().toLowerCase().contains(searchID.getText().toString().toLowerCase())){
//                                    Toast.makeText(DisplaySavedSensorValues.this, "I am here", Toast.LENGTH_SHORT).show();
                                    list.add(model);
                                }
                                else{
//                                    Toast.makeText(DisplaySavedSensorValues.this, model.getSampleID().toString()+"  "+searchID.getText().toString(), Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                            }
                            if (list.size() == 0){
                                SavedValuesModel model = new SavedValuesModel("0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "No matching values are found", "123xxxx");
                                list.add(model);
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
//                       Log.i("check", error.toString());

                        }
                    });
                }

            }
        });
        //When user clicks on home page
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySavedSensorValues.this, SensorDashboard.class);
                startActivity(intent);

            }
        });

    }
    //Function for loading data from database for recycler views
    private void loadData() {
        //Getting values from database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("SavedValues");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                     Toast.makeText(DisplaySavedSensorValues.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
//                     Log.i("check3", dataSnapshot.toString());
                    SavedValuesModel model = dataSnapshot.getValue(SavedValuesModel.class);
                    list.add(model);


                }
                if (list.size() == 0){
                    SavedValuesModel model = new SavedValuesModel("0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "No values are found in database", "123xxxx");
                    list.add(model);
                    adapter.notifyDataSetChanged();
                }
                else{
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("check", error.toString());

            }
        });
    }
}