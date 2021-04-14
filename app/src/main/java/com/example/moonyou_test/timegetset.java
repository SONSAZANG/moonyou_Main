package com.example.moonyou_test;

public class timegetset {
    String time;
    String seat_array;
    String date;
    private String show_id;
    int total_seat;
    int left_seat;

    public String getSeat_array() {
        return seat_array;
    }

    public void setSeat_array(String seat_array) {
        this.seat_array = seat_array;
    }

    public int getTotal_seat() {
        return total_seat;
    }

    public void setTotal_seat(int total_seat) {
        this.total_seat = total_seat;
    }

    public int getLeft_seat() {
        return left_seat;
    }

    public void setLeft_seat(int left_seat) {
        this.left_seat = left_seat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShow_id() {
        return show_id;
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }
}
