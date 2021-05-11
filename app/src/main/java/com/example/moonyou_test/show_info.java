package com.example.moonyou_test;

public class show_info {
    private String image_Path;
    private String title;
    private String period;
    private String show_id;
    private String startday;
    private String finishday;
    private String seat_array;
    private int runtime;
    private int hit;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartday() { return startday; }

    public void setStartday(String startday) { this.startday = startday; }

    public String getFinishday() { return finishday; }

    public void setFinishday(String finishday) { this.finishday = finishday; }

    public String getShow_id() {
        return show_id;
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }

    public String getImage_Path() {
        return image_Path;
    }

    public void setImage_Path(String image_Path) {
        this.image_Path = image_Path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSeat_array() {
        return seat_array;
    }

    public void setSeat_array(String seat_array) {
        this.seat_array = seat_array;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }
}
