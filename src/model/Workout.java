package model;

import java.sql.Date;

public class Workout {
    private int id;
    private int userId;
    private String type;
    private int duration; // in minutes
    private String intensity;
    private Date date;

    public Workout(int id, int userId, String type, int duration, String intensity, Date date) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.duration = duration;
        this.intensity = intensity;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
