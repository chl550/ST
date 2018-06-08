package com.example.coryliang.scheduletracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cory Liang on 6/3/2018.
 */

public class SchedulePair implements Parcelable {
    //might change location to Location object
    private String location;
    private String task;
    private boolean done;
    public SchedulePair(String one, String two, boolean three) {
        location = one;
        task = two;
        done = three;
    }

    protected SchedulePair(Parcel in) {
        location = in.readString();
        task = in.readString();
        done = in.readByte() != 0;
    }

    public static final Creator<SchedulePair> CREATOR = new Creator<SchedulePair>() {
        @Override
        public SchedulePair createFromParcel(Parcel in) {
            return new SchedulePair(in);
        }

        @Override
        public SchedulePair[] newArray(int size) {
            return new SchedulePair[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(location);
        parcel.writeString(task);
        parcel.writeByte((byte) (done ? 1 : 0));
    }
}
