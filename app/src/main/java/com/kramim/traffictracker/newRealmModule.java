package com.kramim.traffictracker;

import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;

@RealmModule(allClasses = true)
public class newRealmModule {
    public void apply(){
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .migration(new MainActivity.TheRealmMigration())
                .build();
    }
}
