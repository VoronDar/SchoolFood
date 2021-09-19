package com.astery.ccabp.model.cloud_database.pogo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GotTask {

    @SerializedName("date")
    @Expose
    private String day;
    @SerializedName("enable")
    @Expose
    private boolean enable;
    @SerializedName("task")
    @Expose
    private boolean task;
    @SerializedName("breakfast")
    @Expose
    private boolean breakfast;
    @SerializedName("lanch")
    @Expose
    private boolean lanch;
    @SerializedName("snack10")
    @Expose
    private boolean snack10;
    @SerializedName("snack15")
    @Expose
    private boolean snack15;

    public GotTask() {
    }

    public GotTask(String day, boolean breakfast, boolean lanch, boolean snack10, boolean snack15) {
        super();
        this.day = day;
        this.breakfast = breakfast;
        this.lanch = lanch;
        this.snack10 = snack10;
        this.snack15 = snack15;
    }


    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isLanch() {
        return lanch;
    }

    public void setLanch(boolean lanch) {
        this.lanch = lanch;
    }

    public boolean isSnack10() {
        return snack10;
    }

    public void setSnack10(boolean snack10) {
        this.snack10 = snack10;
    }

    public boolean isSnack15() {
        return snack15;
    }

    public void setSnack15(boolean snack15) {
        this.snack15 = snack15;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean getTask() {
        return task;
    }

    public void setTask(boolean task) {
        this.task = task;
    }
}