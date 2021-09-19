package com.astery.ccabp.model.cloud_database.pogo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GotHolyday {

    @SerializedName("start")
    @Expose
    private int start;
    @SerializedName("end")
    @Expose
    private int end;

    private int childId;
    private int month;

    public GotHolyday(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }


    @Override
    public String toString() {
        return "GotHolyday{" +
                "start=" + start +
                ", end=" + end +
                ", childId=" + childId +
                ", month=" + month +
                '}';
    }

    public GotHolyday(int childId, int start, int end, int month) {
        this.start = start;
        this.end = end;
        this.childId = childId;
        this.month = month;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public static int generateMonth(int year, int month){
        return year*100 + month;
    }
}