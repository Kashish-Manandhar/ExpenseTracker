package com.example.expensetracker;

public class Transaction {
    private String description;
    private double price;
    private String date,time,type;
    long millis;

    public Transaction() {
    }

    public Transaction(String description, double price, String date, String time, String type, long millis) {
        this.description = description;
        this.price = price;
        this.date = date;
        this.time = time;
        this.type = type;
        this.millis = millis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }
}
