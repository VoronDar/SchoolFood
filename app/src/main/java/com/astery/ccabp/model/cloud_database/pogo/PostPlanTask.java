package com.astery.ccabp.model.cloud_database.pogo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostPlanTask {

    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("childId")
    @Expose
    private int childId;
    @SerializedName("classId")
    @Expose
    private int classId;
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

    public PostPlanTask() {
    }

    public PostPlanTask(int day, int childId, int classId, boolean breakfast, boolean lanch, boolean snack10, boolean snack15) {
        super();
        this.day = day;
        this.childId = childId;
        this.classId = classId;
        this.breakfast = breakfast;
        this.lanch = lanch;
        this.snack10 = snack10;
        this.snack15 = snack15;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
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

}