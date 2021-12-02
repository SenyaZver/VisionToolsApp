package com.example.visiontoolsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.visiontoolsapp.R;

public class MainActivity extends AppCompatActivity {



    private Button GoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoButton = findViewById(R.id.GoButton);
        GoButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SettingsActivity.class);
            view.getContext().startActivity(intent);});



    }



}