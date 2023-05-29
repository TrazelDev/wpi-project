package com.kramim.traffictracker;

import android.annotation.SuppressLint;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeMan {
    public static Date getDate()
    {
        // getting the time:
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getDefault();
        Date date = cal.getTime();
        // Add three hours to the local time
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, 3);
        date = cal.getTime();

        return date;
    }

    @SuppressLint("NewApi")
    public static Duration DateDiff(Date start, Date end)
    {
        // Calculate the time difference in milliseconds
        long timeDiff = end.getTime() - start.getTime();

        return Duration.ofMillis(timeDiff);
    }
}
