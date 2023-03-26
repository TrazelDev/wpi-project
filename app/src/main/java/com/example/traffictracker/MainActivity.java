package com.example.traffictracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.Manifest;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RadioGroup transportationOptions;
    private Button startButton;
    private Button stopButton;
    private Button trafficJamButton;
    private TextView checking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        checking = findViewById(R.id.textView1);

        transportationOptions = findViewById(R.id.transportation_options);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_foot_button);
        stopButton.setVisibility(View.GONE);
        trafficJamButton = findViewById(R.id.traffic_jam_button);
        trafficJamButton.setVisibility(View.GONE);

        startButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startButton.getText().equals("Start")) {
                    startButton.setText("End");
                    startButton.setBackgroundColor(Color.RED);
                    transportationOptions.setEnabled(false);
                    stopButton.setVisibility(View.VISIBLE);

                    int checkedId = transportationOptions.getCheckedRadioButtonId();

                    if (checkedId == R.id.car_button) {
                        findViewById(R.id.bus_button).setEnabled(false);
                        findViewById(R.id.foot_button).setEnabled(false);
                        findViewById(R.id.bicycle_button).setEnabled(false);
                        trafficJamButton.setVisibility(View.VISIBLE);
                    } else if (checkedId == R.id.bus_button) {
                        findViewById(R.id.car_button).setEnabled(false);
                        findViewById(R.id.foot_button).setEnabled(false);
                        findViewById(R.id.bicycle_button).setEnabled(false);
                        trafficJamButton.setVisibility(View.VISIBLE);
                    } else if (checkedId == R.id.foot_button) {
                        findViewById(R.id.car_button).setEnabled(false);
                        findViewById(R.id.bus_button).setEnabled(false);
                        findViewById(R.id.bicycle_button).setEnabled(false);
                        trafficJamButton.setVisibility(View.GONE);
                    } else if (checkedId == R.id.bicycle_button) {
                        findViewById(R.id.car_button).setEnabled(false);
                        findViewById(R.id.bus_button).setEnabled(false);
                        findViewById(R.id.foot_button).setEnabled(false);
                        trafficJamButton.setVisibility(View.GONE);
                    }


                } else {
                    transportationOptions.setEnabled(true);
                    startButton.setText("Start");
                    startButton.setBackgroundColor(Color.GREEN);
                    stopButton.setText("Stop");
                    stopButton.setVisibility(View.GONE);
                    trafficJamButton.setText("Traffic Jam");
                    trafficJamButton.setVisibility(View.GONE);
                    findViewById(R.id.car_button).setEnabled(true);
                    findViewById(R.id.bus_button).setEnabled(true);
                    findViewById(R.id.foot_button).setEnabled(true);
                    findViewById(R.id.bicycle_button).setEnabled(true);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopButton.getText().equals("Stop")) {
                    stopButton.setText("End of Stop");
                } else {
                    stopButton.setText("Stop");
                }
            }
        });

        trafficJamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trafficJamButton.getText().equals("Traffic Jam")) {
                    trafficJamButton.setText("End of Traffic Jam");
                } else {
                    trafficJamButton.setText("Traffic Jam");
                }
            }
        });

        // @Override
        // public void onLocationChanged(Location location) {
        //     double latitude = location.getLatitude();
        //     double longitude = location.getLongitude();
        //
        //     // Do something with the latitude and longitude
        // }
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    @SuppressLint("MissingPermission")
    private void retrieveLocation() {
        checking.setText("work");
        LocationManager manger = (LocationManager) getSystemService(LOCATION_SERVICE);

        manger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

        Location location = manger.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null)
        {
            double lat = location.getLatitude();
            double longitude = location.getLongitude();

            // geocoder is for converting
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());


            try {
                List<Address> addressList = geocoder.getFromLocation(lat, longitude, 1);

                // getting the time:
                Calendar cal = Calendar.getInstance();
                TimeZone timeZone = TimeZone.getDefault();
                Date date = cal.getTime();
                // Add three hours to the local time
                cal.setTime(date);
                cal.add(Calendar.HOUR_OF_DAY, 3);
                date = cal.getTime();

                String timeString = DateFormat.getTimeInstance().format(date);

                checking.setText("lay:" + lat + " long: " + longitude + " time: " + timeString);
            } catch (IOException e) {
                e.printStackTrace();
                checking.setText("fail");
            }
        }

    }


    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);


        if(requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            retrieveLocation();
        }
        else
        {
            // add something:
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
