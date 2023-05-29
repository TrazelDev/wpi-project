package com.kramim.traffictracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.traffictracker.R;

import org.bson.types.ObjectId;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class RegistrationActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private static final String SP_FOLDER_ = "shared_prefs";
    private static final String GENDER_INDEX_ = "genderIndex";
    private static final String SCHOOL_ = "school";
    private static final String USER_ID_ = "user_id";

    private static final String LIVING_AREA_ = "livingArea";
    private RadioGroup radioGroupGender;
    private Spinner schools, neighborhoods;
    private App app;
    public static UserDoc userDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        schools = findViewById(R.id.school_list);
        neighborhoods = findViewById(R.id.neighbor_list);

        ArrayAdapter<String> adapterSchool = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.schools));
        schools.setAdapter(adapterSchool);
        ArrayAdapter<String> adapterNeighbor = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.neighborhoods));
        neighborhoods.setAdapter(adapterNeighbor);

        connectToRealm();
        load_Data();
        //checks if all the registration data isn't at its default value
        if((radioGroupGender.getCheckedRadioButtonId() != -1) && !(schools.getSelectedItemPosition() == Spinner.INVALID_POSITION)
                && !(neighborhoods.getSelectedItemPosition() == Spinner.INVALID_POSITION))
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
        editor.putInt(SCHOOL_, schools.getSelectedItemPosition());
        editor.putInt(LIVING_AREA_, neighborhoods.getSelectedItemPosition());
        editor.apply();


        //database stuff
        Realm realm = Realm.getDefaultInstance();

        try (realm) {
            realm.beginTransaction();

            userDoc = new UserDoc();
            userDoc.set_id(ObjectId.get());
            String gender = ((RadioButton)(findViewById(radioGroupGender.getCheckedRadioButtonId()))).getText().toString();
            userDoc.setGender(gender);

            Resources res = getResources();
            String[] schoolArr = res.getStringArray(R.array.schools);
            userDoc.setSchool(schoolArr[schools.getSelectedItemPosition()]);

            String[] neighborArr = res.getStringArray(R.array.neighborhoods);
            userDoc.setNeighborhood(neighborArr[neighborhoods.getSelectedItemPosition()]);

            editor.putString(USER_ID_, userDoc.get_id().toString());
            editor.apply();
            realm.insert(userDoc);
            realm.commitTransaction();

        } catch (Exception e) {
            Log.e("SHIT", "Couldn't save user: \n" + e);
            realm.cancelTransaction();
        }

        // User input is valid, so show a pop-up window with a success message and a button to proceed to the main activity
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Registration successful!")
                .setPositiveButton("OK", (dialog, id) -> {
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void load_Data() {
        sp = getSharedPreferences(SP_FOLDER_, 0);
        int genderIndex = sp.getInt(GENDER_INDEX_, -1);
        if(genderIndex != -1)
            ((RadioButton)radioGroupGender.getChildAt(genderIndex)).setChecked(true);

        schools.setSelection(sp.getInt(SCHOOL_, Spinner.INVALID_POSITION));
        neighborhoods.setSelection(sp.getInt(LIVING_AREA_, Spinner.INVALID_POSITION));
        Realm realm = Realm.getDefaultInstance();

        userDoc = new UserDoc();
        userDoc.set_id(new ObjectId(sp.getString(USER_ID_, "644e9f068592a8446f5e2490")));
        String gender = "Attack Helicopter";
        if(genderIndex >= 0)
             gender = ((RadioButton)radioGroupGender.getChildAt(genderIndex)).getText().toString();
        userDoc.setGender(gender);

        int schoolIndex = sp.getInt(SCHOOL_, 0);
        userDoc.setSchool(getResources().getStringArray(R.array.schools)[schoolIndex]);
        int neighborIndex = sp.getInt(LIVING_AREA_, 0 );
        userDoc.setNeighborhood(getResources().getStringArray(R.array.neighborhoods)[neighborIndex]);
    }

    private void connectToRealm() {

        Realm.init(this);
        String appID = "realmsyncapp-flfge";
        app = new App(new AppConfiguration.Builder(appID)
                .build());

        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated anonymously.");
                User user = result.get();
                SyncConfiguration config = new SyncConfiguration.Builder(
                        user)
                        .schemaVersion(3)
                        .allowWritesOnUiThread(true)
                        .allowQueriesOnUiThread(true)
                        .modules(new newRealmModule())
                        .build();
                Realm.getInstanceAsync(config, new Realm.Callback() {
                    @Override
                    public void onSuccess(@NonNull Realm realm) {
                        Log.v("TEST", "Successfully opened a realm.");
                        Log.v("Realm", "Realm Path: " + realm.getPath());

                    }

                });
                FutureTask<String> task = new FutureTask<>(new MainActivity.BackgroundQuickStart(app.currentUser()), "test");
                ExecutorService executorService = Executors.newFixedThreadPool(2);
                executorService.execute(task);
                Realm.setDefaultConfiguration(config);
                Log.v("Realm", "Realm Schema: " + Realm.getDefaultInstance().getSchema());



            }

            else {
                Log.e("EXAMPLE", "Failed to log in: " + result.getError().getErrorMessage());
            }
        });

    }

}
