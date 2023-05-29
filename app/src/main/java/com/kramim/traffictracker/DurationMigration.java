package com.kramim.traffictracker;

import android.util.Log;

import org.bson.types.ObjectId;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class DurationMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        // Check the old version to determine which migration steps to apply
        if (oldVersion == 1) {
            // Perform migration for version 0 to version 1
            schema.get("TripDoc")
                    .addField("durationString", String.class)
                    .addField("id", Object.class)
                    .removeField("_id")
                    .addField("_duration", Integer.class)
                    .removeField("duration");


            oldVersion++;
        }
        if (oldVersion == 2) {
            Log.v("TEST", (schema.get("TripDoc")
                    .addField("duration", Integer.class, FieldAttribute.REQUIRED)
                    .removeField("_duration")
                    .addField("_id", ObjectId.class, FieldAttribute.PRIMARY_KEY)
                    .removeField("id").toString()));
        }
    }
}
