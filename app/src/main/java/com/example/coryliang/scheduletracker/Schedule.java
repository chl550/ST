package com.example.coryliang.scheduletracker;

import android.location.Location;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;


/**
 * Created by Cory Liang on 6/2/2018.
 */

public class Schedule {
    private static HashMap<Date, String[]> schedule;
    public Schedule() {
        schedule = new HashMap<Date,String[]>();
    }
    public void addTask(Date time, String location, String task) {
        String[] array = new String[2];
        array[0] = location;
        array[1] = task;
        schedule.put(time, array);
    }
    public void deleteTask(Date time, String location, String task) {
        Set<Date> keys = schedule.keySet();
        Iterator<Date> it = keys.iterator();
        while (it.hasNext()) {
            Date time2 = it.next();
            if (time.equals(time2)) {
                if (schedule.get(time2)[0] == location && schedule.get(time2)[1] == task) {
                    schedule.remove(time2);
                    break;
                }
            }
        }
    }
    public void modifyTask(Date time, String location, String task) {
        schedule.get(time)[0] = location;
        schedule.get(time)[1] = task;
    }

}
