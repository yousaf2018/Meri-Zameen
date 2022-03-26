package com.example.meri_zameen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {
    ImageButton btn1;
    Button btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        btn1 = findViewById(R.id.btnImageAnalysis);
        btn2 = findViewById(R.id.btnImageAnalysis2);
        //When user clicks on image
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ImageAnalysis.class);
                startActivity(intent);
            }
        });
        //When user clicks on button
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ImageAnalysis.class);
                startActivity(intent);
            }

        });

    }
}