package com.example.jeon.tabtest;

/**
 * Created by S on 2016-04-23.
 */
public class RecordItem {

    private int id;
    private int type;
    private String takesTime;
    private String amount;
    private String day;
    private String time;

    public RecordItem(int _id, int _type, String takes_time, String amount, String day, String time) {

        this.id = _id;
        this.type = _type;
        this.takesTime = takes_time;
        this.amount = amount;
        this.day = day;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getTakesTime() {
        return takesTime;
    }

    public String getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public int getType() {
        return type;
    }
}
