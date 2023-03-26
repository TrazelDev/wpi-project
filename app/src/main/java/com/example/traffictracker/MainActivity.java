package com.example.traffictracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private RadioGroup transportationOptions;
    private Button startButton;
    private Button stopButton;
    private Button trafficJamButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        transportationOptions = findViewById(R.id.transportation_options);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_foot_button);
        stopButton.setVisibility(View.GONE);
        trafficJamButton = findViewById(R.id.traffic_jam_button);
        trafficJamButton.setVisibility(View.GONE);

        startButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

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
    }
}
