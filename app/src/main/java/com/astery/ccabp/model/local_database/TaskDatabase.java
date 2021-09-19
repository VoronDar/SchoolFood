package com.astery.ccabp.model.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.astery.ccabp.model.local_database.things.TaskDbHelper;
import com.astery.ccabp.model.things.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_BREAKFAST;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_BREAKFAST_MAX;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_CHILD_ID;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_DAY;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_LUNCH;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_LUNCH_MAX;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_SNACK;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_SNACK_MAX;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.COLUMN_TYPE;
import static com.astery.ccabp.model.local_database.things.TaskContract.TaskEntry.TABLE_NAME;

public class TaskDatabase {
    private SQLiteDatabase database;
    protected Context context;

    private static TaskDatabase cb;

    public static TaskDatabase getInstance(Context context){
        if (cb != null && cb.database.isOpen()){
            return cb;
        }
        return new TaskDatabase(context);
    }


    private TaskDatabase(Context context) {
        this.context = context;
        cb = this;
        open();
    }


    public void open(){
        SQLiteOpenHelper dbHelper = new TaskDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        if (database.isOpen())
            database.close();
    }
    private ContentValues fillContentValues(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DAY, Task.timeToInt(task.getTimeStamp()));
        contentValues.put(COLUMN_CHILD_ID, task.getChildId());
        contentValues.put(COLUMN_BREAKFAST , task.isBreakfast());
        contentValues.put(COLUMN_TYPE , getType(task));
        contentValues.put(COLUMN_BREAKFAST_MAX , task.isBreakfastMax());
        contentValues.put(COLUMN_LUNCH , task.isLanch());
        contentValues.put(COLUMN_LUNCH_MAX , task.isLanchMax());
        contentValues.put(COLUMN_SNACK , task.isSnak10());
        contentValues.put(COLUMN_SNACK_MAX , task.isSnak15());
        return contentValues;
    }

    private Task getSchedule(Cursor cursor){
        return new Task((cursor.getInt(cursor.getColumnIndex(COLUMN_CHILD_ID))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_DAY))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_BREAKFAST)))>0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_BREAKFAST_MAX)))>0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_LUNCH)))>0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_LUNCH_MAX)))>0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_SNACK)))>0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_SNACK_MAX)))>0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE))));
    }



    private int getType(Task task){
        return (!task.isAccepted())?3:(task.isTask()?2:1);
    }

    public void update(Task task) {
        Log.i("task", task.toString());
        ContentValues cv = fillContentValues(task);
        if (database.update(TABLE_NAME, cv,
                COLUMN_DAY + " = '" + Task.timeToInt(task.getTimeStamp()) + "' AND " + COLUMN_CHILD_ID + " = '" + task.getChildId() +
                        "' AND " + COLUMN_TYPE + " = '" + getType(task) + "'", null) <= 0) {
            database.insert(TABLE_NAME, null, cv);
        }
    }

    public Task getTask(int childId, int day){
        Log.i("main", childId + " " + day);
        Task task;
        Cursor cursor = database.query(TABLE_NAME, null,
                COLUMN_DAY + " = '" + day + "' AND " + COLUMN_CHILD_ID + " = '" + childId + "'",  null, null,
                null, null, null);
        //
        Task taskDisabled = null;
        Task taskAccepted = null;
        while (cursor.moveToNext()) {
            task = getSchedule(cursor);
            Log.i("main", task.toString());
            if (task.isAccepted())
                taskAccepted = task;
            else
                taskDisabled = task;
        }

        cursor.close();
        if (taskAccepted != null)
            return taskAccepted;
        else
            return taskDisabled;
    }

    public void delete(){
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        database.execSQL(clearDBQuery);
    }

    public ArrayList<Task> getEverything(){
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            tasks.add(getSchedule(cursor));
        }
        cursor.close();
        return tasks;
    }
    public ArrayList<Task> getEverything(int childId){
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_CHILD_ID + " = '" + childId + "'", null,
                null, null, null);
        while (cursor.moveToNext()) {
            tasks.add(getSchedule(cursor));
        }
        cursor.close();
        return tasks;
    }

    public ArrayList<Task> getTasksByPeriod(int childId, Calendar startDayC, Calendar endDayc) {
        // стартовый и конечный числа включительно
        int startDay = Task.timeToInt(startDayC);
        int endDay = Task.timeToInt(endDayc);
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_DAY + " <= '" + endDay
                        + "' AND " + COLUMN_DAY + " >= '" + startDay
                        + "' AND " + COLUMN_CHILD_ID + " = '" + childId + "'", null,
                null, null, COLUMN_DAY);
        while (cursor.moveToNext()) {
            Task t = getSchedule(cursor);
            boolean justAdd = true;
            for (int i = 0; i < tasks.size(); i++){
                Task task = tasks.get(i);
                if (task.getTimeStamp().equals(t.getTimeStamp()) && !t.isAccepted()){
                    tasks.remove(task);
                    tasks.add(t);
                    justAdd = false;
                    break;
                }
                else if (task.getTimeStamp().equals(t.getTimeStamp())){
                    justAdd = false;
                    break;
                }
            }
            if (justAdd)
            tasks.add(t);
        }
        cursor.close();
        return tasks;
    }



    public void deleteOld(int now){
        database.delete(TABLE_NAME, COLUMN_DAY + " < '" + (now-40) + "'", null);

    }



}
