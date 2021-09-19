package com.astery.ccabp.model.transport_preferences;

import android.content.Context;
import android.util.Log;

import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.things.Task;
import com.astery.ccabp.view.activities.MainFragments.CalendarFragment;

import java.util.ArrayList;

public class TPStorage {
    public static void loadTodayTask(Context context, Task task){

        TransportPreferences.setTodayCheck(context, task.isBreakfast(), 0, task.getChildId());
        TransportPreferences.setTodayCheck(context, task.isLanch(), 1, task.getChildId());
        TransportPreferences.setTodayCheck(context, task.isSnak10(), 2, task.getChildId());
        TransportPreferences.setTodayCheck(context, task.isSnak15(), 3, task.getChildId());
    }

    public static void loadPlanTask(Context context, Task task, int day) {
        Log.i("main", task.getChildId());
        TransportPreferences.setPlanAccept(context, task.isBreakfast(), day,  task.getChildId(), 0);
        TransportPreferences.setPlanAccept(context, task.isLanch(),  day, task.getChildId(), 1);
        TransportPreferences.setPlanAccept(context, task.isSnak10(),  day,  task.getChildId(), 2);
        TransportPreferences.setPlanAccept(context, task.isSnak15(),  day,  task.getChildId(), 3);
        saveTemperPlan(context, task ,day);
    }

    public static void saveTemperPlan(Context context, Task task, int day) {
        TransportPreferences.setPlanSavedAccept(context, task.isBreakfast(), day,  task.getChildId(), 0);
        TransportPreferences.setPlanSavedAccept(context, task.isLanch(),  day, task.getChildId(), 1);
        TransportPreferences.setPlanSavedAccept(context, task.isSnak10(),  day,  task.getChildId(), 2);
        TransportPreferences.setPlanSavedAccept(context, task.isSnak15(),  day,  task.getChildId(), 3);
    }


    public static void saveTemperTask(Context context, Task task) {
        TransportPreferences.setTodaySaveCheck(context, task.isBreakfast(), 0, task.getChildId());
        TransportPreferences.setTodaySaveCheck(context, task.isLanch(),  1, task.getChildId());
        TransportPreferences.setTodaySaveCheck(context, task.isSnak10(),  2,  task.getChildId());
        TransportPreferences.setTodaySaveCheck(context, task.isSnak15(),  3,  task.getChildId());

    }


    private static void removeTemperTask(Context context,String childId) {
        TransportPreferences.setTodaySaveCheck(context, TransportPreferences.getTodayCheck(context, 0, childId)
                , 0,  childId);
        TransportPreferences.setTodaySaveCheck(context, TransportPreferences.getTodayCheck(context, 1, childId)
                ,  1, childId);
        TransportPreferences.setTodaySaveCheck(context, TransportPreferences.getTodayCheck(context, 2, childId)
                ,  2,  childId);
        TransportPreferences.setTodaySaveCheck(context, TransportPreferences.getTodayCheck(context, 3, childId)
                ,  3,  childId);
    }


    public static void removeTemperTask(Context context, ArrayList<Child> children){
        for (Child child : children){
            removeTemperTask(context, child.getId());
        }
    }

    private static void removeTemperPlan(Context context, int day, String childId){
        TransportPreferences.setPlanSavedAccept(context, TransportPreferences.getPlanAccept(context, day, childId, 0)
                , day,  childId, 0);
        TransportPreferences.setPlanSavedAccept(context, TransportPreferences.getPlanAccept(context, day, childId, 1)
                ,  day, childId, 1);
        TransportPreferences.setPlanSavedAccept(context, TransportPreferences.getPlanAccept(context, day, childId, 2)
                ,  day,  childId, 2);
        TransportPreferences.setPlanSavedAccept(context, TransportPreferences.getPlanAccept(context, day, childId, 3)
                ,  day,  childId, 3);
    }

    public static void removeTemperPlan(Context context, ArrayList<Child> children){
        for (Child child : children){
            for (int i = 0; i < 6; i++){
                removeTemperPlan(context, i, child.getId());
            }
        }
    }


}
