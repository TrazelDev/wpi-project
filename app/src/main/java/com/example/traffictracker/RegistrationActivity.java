package com.example.traffictracker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private static final String SP_FOLDER_ = "shared_prefs";
    private static final String GENDER_INDEX_ = "genderIndex";
    private static final String SCHOOL_ = "school";

    private static final String LIVING_AREA_ = "livingArea";
    private RadioGroup radioGroupGender;
    private EditText editTextSchool, editTextLivingArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        editTextSchool = findViewById(R.id.editTextSchool);
        editTextLivingArea = findViewById(R.id.editTextLivingArea);
        load_Data();
        //checks if all the registration data isn't at its default value
        if((radioGroupGender.getCheckedRadioButtonId() != -1) && !(editTextSchool.getText().toString().equals(""))
                && !(editTextLivingArea.getText().toString().equals("")))
        {
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void registerUser(View view) {
        // Get a reference to the gender radio group and the school input field


        // Validate the user's input
        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            // No radio button is checked
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(GENDER_INDEX_,radioGroupGender.indexOfChild(findViewById(radioGroupGender.getCheckedRadioButtonId())));
        editor.putString(SCHOOL_, editTextSchool.getText().toString());
        editor.putString(LIVING_AREA_, editTextLivingArea.getText().toString());
        editor.commit();

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

    public void load_Data() {
        sp = getSharedPreferences(SP_FOLDER_, 0);
        int genderIndex = sp.getInt(GENDER_INDEX_, -1);
        if(genderIndex != -1)
            ((RadioButton)radioGroupGender.getChildAt(genderIndex)).setChecked(true);

        editTextSchool.setText(sp.getString(SCHOOL_, null));
        editTextLivingArea.setText(sp.getString(LIVING_AREA_, null));
    }

}
