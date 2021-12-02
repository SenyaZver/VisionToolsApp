package com.example.visiontoolsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.visiontoolsapp.R;


public class SettingsActivity extends AppCompatActivity {

//    private static final String[] CAMERA_PERMISSION = new String[] {Manifest.permission.CAMERA};
//    private static final int CAMERA_REQUEST_CODE = 10;

    boolean viewFlip_Choice;
    int colourCorrection_Choice;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox viewFlipBox = findViewById(R.id.checkBoxFlip);

        viewFlipBox.setOnCheckedChangeListener((buttonView, isChecked) -> viewFlip_Choice = isChecked);





        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case -1:
                    colourCorrection_Choice = -1;
                    break;
                case R.id.radioButton_GBR:
                    colourCorrection_Choice = 0;
                    break;
                case R.id.radioButton_BRG:
                    colourCorrection_Choice = 1;
                    break;
                case R.id.radioButton_GRB:
                    colourCorrection_Choice = 2;
                    break;
                case R.id.radioButton_BGR:
                    colourCorrection_Choice = 3;
                    break;
                case R.id.radioButton_RBG:
                    colourCorrection_Choice = 4;
                    break;
                case R.id.radioButton_No_colourChange:
                    colourCorrection_Choice = -1;
                    break;
            }
        });



        Button goButton = findViewById(R.id.SettingsGoButton);
        goButton.setOnClickListener(view -> {
//            if (hasCameraPermission()) {
//                enableCamera();
//            } else {
//                requestPermission();
//            }

            boolean viewFlip_Choice_made = viewFlip_Choice;
            int colourCorrection_Choice_made = colourCorrection_Choice;

            Intent intent = new Intent(SettingsActivity.this, CameraActivity.class);
            intent.putExtra("viewFlip_Choice", viewFlip_Choice_made);
            intent.putExtra("colourCorrection_Choice", colourCorrection_Choice_made);
            view.getContext().startActivity(intent);});
    }

//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(
//                this,
//                CAMERA_PERMISSION,
//                CAMERA_REQUEST_CODE
//        );
//    }
//
//    private void enableCamera() {
//        Intent intent = new Intent(this, CameraActivity.class);
//        startActivity(intent);
//    }
//
//    private boolean hasCameraPermission()  {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//    }
//
//

}