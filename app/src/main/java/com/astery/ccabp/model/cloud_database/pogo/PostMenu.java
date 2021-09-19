package com.astery.ccabp.model.cloud_database.pogo;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostMenu {

    @SerializedName("breakfast")
    @Expose
    private List<String> breakfast = null;
    @SerializedName("breakfastMax")
    @Expose
    private List<String> breakfastMax = null;
    @SerializedName("lanch")
    @Expose
    private List<String> lanch = null;
    @SerializedName("lanchMax")
    @Expose
    private List<String> lanchMax = null;
    @SerializedName("homeId")
    @Expose
    private int homeId;
    @SerializedName("snack10")
    @Expose
    private List<String> snack10 = null;
    @SerializedName("snack15")
    @Expose
    private List<String> snack15 = null;
    @SerializedName("week")
    @Expose
    private int week;
    @SerializedName("weekday")
    @Expose
    private int weekday;
    @SerializedName("young")
    @Expose
    private boolean young;


    private int id = -1;

    public PostMenu() {
    }

    public PostMenu(List<String> breakfast, List<String> lanch, List<String> snack10, List<String> snack15, int week, int weekday) {
        super();
        this.breakfast = breakfast;
        this.lanch = lanch;
        this.snack10 = snack10;
        this.snack15 = snack15;
        this.week = week;
        this.weekday = weekday;
    }


    public PostMenu(int id, int homeId, int weekday, int week, boolean young, List<String> breakfast,
                    List<String> breakfastMax, List<String> lunch, List<String> lunchMax,
                    List<String> snack, List<String> snackMax){
        this.breakfast = breakfast;
        this.breakfastMax = breakfastMax;
        this.lanch = lunch;
        this.lanchMax = lunchMax;
        this.snack10 = snack;
        this.snack15 = snackMax;
        this.week = week;
        this.weekday = weekday;
        this.id = id;
        this.homeId = homeId;
        this.young = young;
    }


    public int getWeek() {
        return week;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public List<String> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<String> breakfast) {
        this.breakfast = breakfast;
    }

    public List<String> getLanch() {
        return lanch;
    }

    public void setLanch(List<String> lanch) {
        this.lanch = lanch;
    }

    public List<String> getSnack10() {
        return snack10;
    }

    public void setSnack10(List<String> snack10) {
        this.snack10 = snack10;
    }

    public List<String> getSnack15() {
        return snack15;
    }

    public void setSnack15(List<String> snack15) {
        this.snack15 = snack15;
    }

    public int getHomeId() {
        return homeId;
    }

    public List<String> getBreakfastMax() {
        return breakfastMax;
    }

    public List<String> getLanchMax() {
        return lanchMax;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public void setBreakfastMax(List<String> breakfastMax) {
        this.breakfastMax = breakfastMax;
    }

    public void setLanchMax(List<String> lanchMax) {
        this.lanchMax = lanchMax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isYoung() {
        return young;
    }

    public void setYoung(boolean young) {
        this.young = young;
    }

    @Override
    public String toString() {
        return "PostMenu{" +
                "breakfast=" + breakfast +
                ", breakfastMax=" + breakfastMax +
                ", lanch=" + lanch +
                ", lanchMax=" + lanchMax +
                ", homeId=" + homeId +
                ", snack10=" + snack10 +
                ", snack15=" + snack15 +
                ", week=" + week +
                ", weekday=" + weekday +
                ", young=" + young +
                ", id=" + id +
                '}';
    }
}