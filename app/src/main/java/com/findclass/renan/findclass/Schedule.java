package com.findclass.renan.findclass;

public class Schedule {
   private String hour;
   private String user;

    public Schedule(String hour, String user) {
        this.hour = hour;
        this.user = user;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
