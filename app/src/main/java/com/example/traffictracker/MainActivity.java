package com.example.traffictracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.Manifest;
import android.widget.TextView;



import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmFieldType;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.mongodb.sync.SyncSession;
import io.realm.mongodb.sync.Sync;

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
    private Trip trip;
    private Handler handler;
    private App app;
    private Realm uiThreadRealm;

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
        startButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark, null));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checking = findViewById(R.id.textView1);
        trip = null;

        connectToRealm();
        
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (trip != null) {
                    int milliSec = trip.getTrackerState() * 1000;
                    if (milliSec == 0) {
                        checking.setText(trip.addPoint(getLocation()));
                        handler.postDelayed(this, milliSec); // Schedule the task again in 1 seconds
                    } else {
                        handler.postDelayed(this, 2000); // Schedule the task again in 1 seconds
                    }
                } else {
                    handler.postDelayed(this, 2000); // Schedule the task again in 1 seconds
                }
            }
        }, 1000); // Start the task after 1 second

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // function that is in charge of operations that needs to happen when the start and end buttons are pressed
            public void onClick(View view) {
                // checking if no was no option that was selected
                if (!(transportationOptions.getCheckedRadioButtonId() != R.id.car_button &&
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
                        saveTrip();
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
                if (!point.checkEmpty()) {
                    if (stopButton.getText().equals("Stop")) {
                        stopButton.setText("End of Stop");
                        trip.addStop(point);
                        trip.setState(Trip.MovingStates.STOP);
                    } else {
                        stopButton.setText("Stop");
                        trip.updateStop(point);
                        trip.backMainState();
                    }
                } else {
                    // error something
                }
            }
        });
        trafficJamButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                Point point = getLocation();
                if (!point.checkEmpty()) {
                    if (trafficJamButton.getText().equals("Traffic Jam")) {
                        trafficJamButton.setText("End of Traffic Jam");
                        trip.addJam(point);
                        trip.setState(Trip.MovingStates.JAM);
                    } else {
                        trafficJamButton.setText("Traffic Jam");
                        trip.updateJam(point);
                        trip.backMainState();
                    }
                } else {
                    // error something
                }
            }
        });

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
                        .modules(new newRealmModule())
                        .build();
                Realm.getInstanceAsync(config, new Realm.Callback() {
                    @Override
                    public void onSuccess(@NonNull Realm realm) {
                        Log.v("TEST", "Successfully opened a realm.");
                        Log.v("Realm", "Realm Path: " + realm.getPath());

                    }

                });
                FutureTask<String> task = new FutureTask<>(new BackgroundQuickStart(app.currentUser()), "test");
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




    private void addChangeListenerToRealm(Realm realm) {
        // all tasks in the realm
        RealmResults<Task> tasks = uiThreadRealm.where(Task.class).findAllAsync();
        tasks.addChangeListener((collection, changeSet) -> {
            // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
            OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
            for (OrderedCollectionChangeSet.Range range : deletions) {
                Log.v("QUICKSTART", "Deleted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));
            }
            OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
            for (OrderedCollectionChangeSet.Range range : insertions) {
                Log.v("QUICKSTART", "Inserted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
            OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
            for (OrderedCollectionChangeSet.Range range : modifications) {
                Log.v("QUICKSTART", "Updated range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
        });
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Phone has no permission for location tracker.\nplease give access to location tracking in settings and start a new trip after doing so");
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
            if (point.checkEmpty()) {
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

            // Geocoder2 is for converting
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
        } else {
            return new Point(0, 0);
        }
    }



    public void saveTrip() {
        Realm realm = Realm.getDefaultInstance();

        try (realm) {
            realm.beginTransaction();

            TripDoc tripDoc = new TripDoc();
            tripDoc.setDistance(trip.get_totalDistance());
            tripDoc.setDuration(trip.get_tripDuration().toMillis());
            tripDoc.setStart_point(new PointDoc(trip.get_startPoint()));

            RealmList<PointDoc> pointDocs = PointDoc.toRealmList(trip.getRoutePoints());
            tripDoc.setPoints(pointDocs);

            realm.insert(tripDoc);
            realm.commitTransaction();
        } catch (Exception e) {
            Log.v("SHIT", "Couldn't save trip: \n" + e);
            realm.cancelTransaction();
        }
        finally {
            realm.close();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    public static class BackgroundQuickStart implements Runnable {
        User user;
        public BackgroundQuickStart(User user) {
            this.user = user;
        }
        @Override
        public void run() {
            String partitionValue = "BackgroundTracker";
            SyncConfiguration config = new SyncConfiguration.Builder(
                    user,
                    partitionValue)
                    .build();
            Realm backgroundThreadRealm = Realm.getInstance(config);
            Task task = new Task("New Task");
            backgroundThreadRealm.executeTransaction (transactionRealm -> {
                transactionRealm.insert(task);
            });
            // all tasks in the realm
            RealmResults<Task> tasks = backgroundThreadRealm.where(Task.class).findAll();
            // you can also filter a collection
            RealmResults<Task> tasksThatBeginWithN = tasks.where().beginsWith("name", "N").findAll();
            RealmResults<Task> openTasks = tasks.where().equalTo("status", TaskStatus.Open.name()).findAll();
            Task otherTask = tasks.get(0);
            // all modifications to a realm must happen inside of a write block
            backgroundThreadRealm.executeTransaction( transactionRealm -> {
                Task innerOtherTask = transactionRealm.where(Task.class).equalTo("_id", otherTask.get_id()).findFirst();
                innerOtherTask.setStatus(TaskStatus.Complete);
            });
            Task yetAnotherTask = tasks.get(0);
            ObjectId yetAnotherTaskId = yetAnotherTask.get_id();
            // all modifications to a realm must happen inside of a write block
            backgroundThreadRealm.executeTransaction( transactionRealm -> {
                Task innerYetAnotherTask = transactionRealm.where(Task.class).equalTo("_id", yetAnotherTaskId).findFirst();
                innerYetAnotherTask.deleteFromRealm();
            });
            // because this background thread uses synchronous realm transactions, at this point all
            // transactions have completed and we can safely close the realm
            backgroundThreadRealm.close();
        }
    }

    public static class TheRealmMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();

            // Check if migration is needed
            if (oldVersion == 0) {
                // Perform migration from version 0 to version 1
                RealmObjectSchema tripDocSchema = schema.get("TripDoc");

                assert tripDocSchema != null;
                if (tripDocSchema.hasField("duration") && !(tripDocSchema.getFieldType("duration") == RealmFieldType.INTEGER)) {
                    tripDocSchema.removeField("duration")
                            .addField("duration", Long.class);
                }
                else if(!tripDocSchema.hasField("duration"))
                    tripDocSchema.addField("duration", Long.class);

                if (tripDocSchema.hasField("duration") && !(tripDocSchema.getFieldType("durationString") == RealmFieldType.STRING)) {
                    tripDocSchema.removeField("duration")
                            .addField("duration", Long.class);
                }
                else if (!tripDocSchema.hasField("durationString"))
                    tripDocSchema.addField("durationString", String.class);


                oldVersion++;
            }
        }
    }
}
