package com.example.moonyou_test;

public class show_info {
    private String image_Path;
    private String title;
    private String period;
    private String show_id;

    public String getShow_id() {
        return show_id;
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }

    private int runtime;
    private int hit;

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