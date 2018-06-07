package com.example.coryliang.scheduletracker;

/**
 * Created by Cory Liang on 6/3/2018.
 */

public class SchedulePair {
    //might change location to Location object
    private String location;
    private String task;
    private boolean done;
    public SchedulePair(String one, String two, boolean three) {
        location = one;
        task = two;
        done = three;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getTask() {
        return this.task;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public boolean getStatus() {
        return this.done;
    }
    public void setStatus(boolean done) {
        this.done = done;
    }
}
