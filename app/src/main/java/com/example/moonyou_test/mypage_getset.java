package com.example.moonyou_test;

public class mypage_getset {
    private String user;
    private String date;
    private int seatCount;
    private String[] seat_num;
    private String time;
    private String title;
    private String seatName;
    private String image;

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getSeatName() { return seatName; }

    public void setSeatName(String seatName) { this.seatName = seatName; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String[] getSeat_num() {
        return seat_num;
    }

    public void setSeat_num(String[] seat_num) {
        this.seat_num = seat_num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }
};
