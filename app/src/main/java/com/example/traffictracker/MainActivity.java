package com.example.traffictracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.Manifest;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

//tasks:
// 1. create things to do when there is no location access to not destroy the data
// 2. check what is around the school(maybe) cause if you are on the bus and you switch to foot then it will not be at school
// 3. find a way to store the results and move them in the server
public class MainActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RadioGroup transportationOptions;
    private Button startButton;
    private Button stopButton;
    private Button trafficJamButton;

    private TextView checking;
    private ArrayList<Stop> stopList;
    private Trip trip;

    private Handler handler;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        // setting starting things:
        transportationOptions = findViewById(R.id.transportation_options);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_foot_button);
        stopButton.setVisibility(View.GONE);
        trafficJamButton = findViewById(R.id.traffic_jam_button);
        trafficJamButton.setVisibility(View.GONE);
        startButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checking = findViewById(R.id.textView1);
        trip = null;

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(trip != null)
                {
                    int milliSec = trip.getTrackerState() * 1000;
                    if(milliSec == 0)
                    {
                        checking.setText(trip.addPoint(getLocation()));
                        handler.postDelayed(this, milliSec); // Schedule the task again in 1 seconds
                    }
                    else
                    {
                        handler.postDelayed(this, 2000); // Schedule the task again in 1 seconds
                    }
                }
                else
                {
                    handler.postDelayed(this, 1000); // Schedule the task again in 1 seconds
                }
            }
        }, 1000); // Start the task after 1 second

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // function that is in charge of operations that needs to happen when the start and end buttons are pressed
            public void onClick(View view) {
                // checking if no was no option that was selected
                if(!(transportationOptions.getCheckedRadioButtonId() != R.id.car_button &&
                        transportationOptions.getCheckedRadioButtonId() != R.id.bus_button &&
                        transportationOptions.getCheckedRadioButtonId() != R.id.foot_button &&
                        transportationOptions.getCheckedRadioButtonId() != R.id.bicycle_button)
                ) {
                    if (startButton.getText().equals("Start")) {
                        boolean state = startButtonOperations();
                        trip = new Trip(getLocation(), state);
                    } else {
                        endButtonOperations();
                        trip.endTrip(getLocation());
                        checking.setText(trip.returnResults());
                        trip = null;
                    }
                }
            }

            public boolean startButtonOperations() {
                startButton.setText("End");
                startButton.setBackgroundColor(Color.RED);
                transportationOptions.setEnabled(false);
                stopButton.setVisibility(View.VISIBLE);

                switch (transportationOptions.getCheckedRadioButtonId()) {
                    case R.id.car_button:
                        findViewById(R.id.bus_button).setEnabled(false);
                        findViewById(R.id.foot_button).setEnabled(false);
                        findViewById(R.id.bicycle_button).setEnabled(false);
                        trafficJamButton.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.bus_button:
                        findViewById(R.id.car_button).setEnabled(false);
                        findViewById(R.id.foot_button).setEnabled(false);
                        findViewById(R.id.bicycle_button).setEnabled(false);
                        trafficJamButton.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.foot_button:
                        findViewById(R.id.car_button).setEnabled(false);
                        findViewById(R.id.bus_button).setEnabled(false);
                        findViewById(R.id.bicycle_button).setEnabled(false);
                        trafficJamButton.setVisibility(View.GONE);
                        return false;
                    case R.id.bicycle_button:
                        findViewById(R.id.car_button).setEnabled(false);
                        findViewById(R.id.bus_button).setEnabled(false);
                        findViewById(R.id.foot_button).setEnabled(false);
                        trafficJamButton.setVisibility(View.GONE);
                        return false;
                }

                return false;
            }

            public void endButtonOperations() {
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


        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                Point point = getLocation();
                if(!point.checkEmpty())
                {
                    if (stopButton.getText().equals("Stop")) {
                        stopButton.setText("End of Stop");
                        trip.addStop(point);
                        trip.setState(Trip.MovingStates.STOP);
                    }
                    else {
                        stopButton.setText("Stop");
                        trip.updateStop(point);
                        trip.backMainState();
                    }
                }
                else
                {
                    // error something
                }
            }
        });
        trafficJamButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                Point point = getLocation();
                if(!point.checkEmpty()) {
                    if (trafficJamButton.getText().equals("Traffic Jam")) {
                        trafficJamButton.setText("End of Traffic Jam");
                        trip.addJam(point);
                        trip.setState(Trip.MovingStates.JAM);
                    } else {
                        trafficJamButton.setText("Traffic Jam");
                        trip.updateJam(point);
                        trip.backMainState();
                    }
                }
                else
                {
                    // error something
                }
            }
        });

    }

    public void createDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Phone has no permission for location tracker.\npls give an access for the location tracking in the settings and start a new trip after that");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do something
            }
        });
        AlertDialog dialog = builder.create();
        builder.show();
    }

    public Point getLocation() {
        Point point;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            point = retrieveLocation();
            if(point.checkEmpty())
            {
                createDialog();
            }
            return retrieveLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        createDialog();
        return new Point(0, 0);
    }
    @SuppressLint("MissingPermission")
    private Point retrieveLocation() {
        LocationManager manger = (LocationManager) getSystemService(LOCATION_SERVICE);
        manger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        Location location = manger.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Point point;
        if (location != null) {
            point = new Point(location.getLatitude(), location.getLongitude());

            // geocoder is for converting
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());


            try {
                List<Address> addressList = geocoder.getFromLocation(point._latitude, point._longitude, 1);

                // getting the time:
                Calendar cal = Calendar.getInstance();
                TimeZone timeZone = TimeZone.getDefault();
                Date date = cal.getTime();

                // Add three hours to the local time
                cal.setTime(date);
                cal.add(Calendar.HOUR_OF_DAY, 3);
                date = cal.getTime();

                String timeString = DateFormat.getTimeInstance().format(date);

                // checking.setText("lay:" + point._latitude + " long: " + point._longitude + " time: " + timeString);
            } catch (IOException e) {
                e.printStackTrace();
                checking.setText("fail");
            }

            return point;
        }
        else
        {
            return new Point(0, 0);
        }
    }


    public void saveTrip()
    {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
