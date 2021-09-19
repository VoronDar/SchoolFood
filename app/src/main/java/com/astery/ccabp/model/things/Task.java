package com.astery.ccabp.model.things;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.astery.ccabp.model.transport_preferences.TransportPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Task {
    private String id;
    private String childId;
    private final String timeStamp;
    private boolean breakfast;
    private boolean breakfastMax;
    private boolean lanch;
    private boolean lanchMax;
    private boolean snak10;
    private boolean snak15;
    private int group;
    private boolean enable;
    private boolean isTask;
    private boolean accepted = true;

    public Task(String id, String childId, String timeStamp, boolean breakfast, boolean lanch, boolean snak10, boolean snak15, int group) {
        this.id = id;
        this.childId = childId;
        this.timeStamp = timeStamp;
        this.breakfast = breakfast;
        this.lanch = lanch;
        this.snak10 = snak10;
        this.snak15 = snak15;
        this.group = group;
    }


    public Task(String id, String childId, String timeStamp, boolean breakfast, boolean lanch, boolean snak10, boolean snak15, boolean enable, boolean isTask, boolean accepted) {
        this.id = id;
        this.childId = childId;
        this.timeStamp = timeStamp;
        this.breakfast = breakfast;
        this.lanch = lanch;
        this.snak10 = snak10;
        this.snak15 = snak15;
        this.enable = enable;
        this.isTask = isTask;
        this.accepted = accepted;
    }


    public Task(int childId, int time, boolean breakfast, boolean breakfastMax, boolean lanch, boolean lanchMax, boolean snak10, boolean snak15, int type) {
        this.childId = childId + "";
        this.breakfast = breakfast;
        this.breakfastMax = breakfastMax;
        this.lanch = lanch;
        this.lanchMax = lanchMax;
        this.snak10 = snak10;
        this.snak15 = snak15;
        this.timeStamp = intToTime(time);
        Log.i("task", "type: " + type);
        this.accepted = !(type==3);
        this.isTask = (type==2);
        this.enable = (breakfast || lanch || breakfastMax || lanchMax || snak10 || snak15);
    }

    public Task(int childId, int time, boolean breakfast, boolean breakfastMax, boolean lanch, boolean lanchMax, boolean snak10, boolean snak15) {
        this.childId = childId + "";
        this.breakfast = breakfast;
        this.breakfastMax = breakfastMax;
        this.lanch = lanch;
        this.lanchMax = lanchMax;
        this.snak10 = snak10;
        this.snak15 = snak15;
        this.timeStamp = time + "";
    }

    public Task(String childId, String timeStamp, boolean breakfast, boolean lanch, boolean snak10, boolean snak15, int group) {
        this.childId = childId;
        this.timeStamp = timeStamp;
        this.breakfast = breakfast;
        this.lanch = lanch;
        this.snak10 = snak10;
        this.snak15 = snak15;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getTimeStamp() {
        return timeStamp;
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

    public boolean isSnak10() {
        return snak10;
    }

    public void setSnak10(boolean snak10) {
        this.snak10 = snak10;
    }

    public boolean isSnak15() {
        return snak15;
    }

    public void setSnak15(boolean snak15) {
        this.snak15 = snak15;
    }

    public int getGroup() {
        return group;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", childId='" + childId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", breakfast=" + breakfast +
                ", lanch=" + lanch +
                ", snak10=" + snak10 +
                ", snak15=" + snak15 +
                ", group=" + group +
                ", enable=" + enable +
                ", isTask=" + isTask +
                ", accepted=" + accepted +
                '}';
    }

    public static void genPayment(Context context){
        if (Task.breakfastPrice != 0) return;
        Task.breakfastPrice = TransportPreferences.getPayment(context, 0);
        Task.lanchPrice = TransportPreferences.getPayment(context, 1);
        Task.snack10Price = TransportPreferences.getPayment(context, 2);
        Task.snack15Price = TransportPreferences.getPayment(context, 3);
    }

    public static void genPayment(int breakfastPrice, int lanchPrice, int snack10Price, int snack15Price){
        Task.breakfastPrice = breakfastPrice;
        Task.lanchPrice = lanchPrice;
        Task.snack10Price = snack10Price;
        Task.snack15Price = snack15Price;
    }
    private static int breakfastPrice = 0;
    private static int lanchPrice = 0;
    private static int snack10Price = 0;
    private static int snack15Price = 0;

    public static int getPayment(int block){
        switch (block){
            case 0:
                return breakfastPrice;
            case 1:
                return lanchPrice;
            case 2:
                return snack10Price;
            default:
                return snack15Price;
        }
    }

    public static Calendar getDate(String day){
        String[] cal = day.split("\\.");
        return new GregorianCalendar(Integer.parseInt(cal[0]), Integer.parseInt(cal[1])-1, Integer.parseInt(cal[2]));
    }

    public Calendar getDate(){
        return getDate(getTimeStamp());
    }

    public boolean isEnable() {
        return enable;
    }


    public boolean isTask() {
        return isTask;
    }

    public void setTask(boolean task) {
        isTask = task;
    }

    public static int getBreakfastPrice() {
        return breakfastPrice;
    }


    public static int getLanchPrice() {
        return lanchPrice;
    }


    public static int getSnack10Price() {
        return snack10Price;
    }


    public static int getSnack15Price() {
        return snack15Price;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        Log.i("main", "setAccepted " + accepted);
        this.accepted = accepted;
    }


    public boolean isBreakfastMax() {
        return breakfastMax;
    }


    public boolean isLanchMax() {
        return lanchMax;
    }


    @SuppressLint("SimpleDateFormat")
    public static int timeToInt(String timeStamp){
        Date init;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date1 = dateFormat.parse(timeStamp);
            init = dateFormat.parse("2020.01.01");
            assert init != null;
            assert date1 != null;
            long milliseconds = date1.getTime() - init.getTime();
            return (int) (milliseconds / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @SuppressLint("SimpleDateFormat")
    public static int timeToInt(Calendar timeStamp){
        Date init;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date1 = timeStamp.getTime();
            init = dateFormat.parse("2020.01.01");
            assert init != null;
            long milliseconds = date1.getTime() - init.getTime();
            return (int) (milliseconds / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @SuppressLint("SimpleDateFormat")
    public static String intToTime(int time){
        Date init;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            init = dateFormat.parse("2020.01.01");
            Calendar cal = Calendar.getInstance();
            assert init != null;
            cal.setTime(init);
            cal.add(Calendar.DATE, time); //minus number would decrement the days
            return cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH)+1)+ "." + cal.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}
