package com.example.coryliang.scheduletracker;

import android.location.Location;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeMap;


/**
 * Created by Cory Liang on 6/2/2018.
 */

public class Schedule {
    private static Map<Date, SchedulePair> schedule;
    private SchedulePair pair;
    public Schedule() {
        schedule = new TreeMap<Date,SchedulePair>();
    }
    public void addTask(Date time, String location, String task) {
        pair = new SchedulePair(location,task, false);
        schedule.put(time, pair);
    }
    //might change later, will only be able to delete a task assuming person has unique times
    public void deleteTask(Date time, String location, String task) {
        schedule.remove(time);
        /*
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
        */
    }
    public void modifyTask(Date time, String location, String task) {
        schedule.get(time).setLocation(location);
        schedule.get(time).setTask(task);
    }
    public void taskDone(Date time) {
        schedule.get(time).setStatus(true);
    }
    public SchedulePair findTask(Date time) {
        return schedule.get(time);
    }

}
