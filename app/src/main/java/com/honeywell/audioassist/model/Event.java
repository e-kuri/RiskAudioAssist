package com.honeywell.audioassist.model;

import java.util.Date;

public class Event {

    public enum Field{
        Time("time"),
        Type("type"),
        Location("location"),
        Severity("severity");

        private String name;

        Field(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    private Date time;
    private String type;
    private String location;
    private int severity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }
}
