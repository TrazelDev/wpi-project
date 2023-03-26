package com.example.traffictracker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void registerUser(View view) {
        // Get a reference to the gender radio group and the school input field
        RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);
        EditText editTextSchool = findViewById(R.id.editTextSchool);

        // Validate the user's input
        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            // No radio button is checked
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        // User input is valid, so show a pop-up window with a success message and a button to proceed to the main activity
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Registration successful!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
