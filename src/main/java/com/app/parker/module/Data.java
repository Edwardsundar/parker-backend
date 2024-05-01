package com.app.parker.module;

public class Data {
    private long time;
    private String place;
    private String type;

    public Data(long time, String place, String type) {
        this.time = time;
        this.place = place;
        this.type = type;
    }

    // Getters and Setters
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Data{" +
                "time=" + time +
                ", place='" + place + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
