package com.example.traffictracker;

import java.lang.annotation.Annotation;

import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.annotations.RealmModule;
import io.realm.annotations.RealmNamingPolicy;

@RealmModule(allClasses = true)
public class newRealmModule {
    public void apply(){
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .migration(new MainActivity.TheRealmMigration())
                .build();
    }
}
