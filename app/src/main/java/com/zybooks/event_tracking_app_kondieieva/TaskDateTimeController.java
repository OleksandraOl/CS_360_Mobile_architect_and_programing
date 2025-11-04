package com.zybooks.event_tracking_app_kondieieva;

import java.util.Calendar;

public class TaskDateTimeController {
    // converts milliseconds to year/month/day/day of week/hour/minute
    public static int getYear(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(Calendar.YEAR);
    }

    public static int getDay(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDayOfWeek(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(Calendar.MINUTE);
    }

    // convert date back into milliseconds
    public static long toMilliSec(int year, int month, int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

}
