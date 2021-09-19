package com.astery.ccabp.model.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.astery.ccabp.model.local_database.things.PlanDbHelper;
import com.astery.ccabp.model.things.Task;

import java.util.ArrayList;

import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.COLUMN_BREAKFAST;
import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.COLUMN_BREAKFAST_MAX;
import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.COLUMN_CHILD_ID;
import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.COLUMN_LUNCH;
import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.COLUMN_LUNCH_MAX;
import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.COLUMN_SNACK;
import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.COLUMN_SNACK_MAX;
import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.COLUMN_WEEKDAY;
import static com.astery.ccabp.model.local_database.things.PlanContract.PlanEntry.TABLE_NAME;

public class PlanDatabase {
    private SQLiteDatabase database;
    protected Context context;

    private static PlanDatabase cb;

    public static PlanDatabase getInstance(Context context){
        if (cb != null && cb.database.isOpen()){
            return cb;
        }
        return new PlanDatabase(context);
    }


    private PlanDatabase(Context context) {
        this.context = context;
        cb = this;
        open();
    }


    public void open(){
        SQLiteOpenHelper dbHelper = new PlanDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        if (database.isOpen())
            database.close();
    }
    private ContentValues fillContentValues(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_WEEKDAY, Integer.parseInt(task.getTimeStamp()));
        contentValues.put(COLUMN_CHILD_ID, task.getChildId());
        contentValues.put(COLUMN_BREAKFAST , task.isBreakfast());
        contentValues.put(COLUMN_BREAKFAST_MAX , task.isBreakfastMax());
        contentValues.put(COLUMN_LUNCH , task.isLanch());
        contentValues.put(COLUMN_LUNCH_MAX , task.isLanchMax());
        contentValues.put(COLUMN_SNACK , task.isSnak10());
        contentValues.put(COLUMN_SNACK_MAX , task.isSnak15());
        return contentValues;
    }

    private Task getTask(Cursor cursor) {
        return new Task((cursor.getInt(cursor.getColumnIndex(COLUMN_CHILD_ID))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_WEEKDAY))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_BREAKFAST))) > 0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_BREAKFAST_MAX))) > 0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_LUNCH))) > 0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_LUNCH_MAX))) > 0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_SNACK))) > 0,
                (cursor.getInt(cursor.getColumnIndex(COLUMN_SNACK_MAX))) > 0);
    }

    public void update(Task task){
        Log.i("task", task.toString());
        ContentValues  cv = fillContentValues(task);
        if (database.update(TABLE_NAME, cv,
                COLUMN_WEEKDAY + " = '" + task.getTimeStamp() + "' AND " + COLUMN_CHILD_ID + " = '" + task.getChildId() + "'",  null) <= 0) {
            database.insert(TABLE_NAME, null, cv);
        }
    }

    public Task getTask(int childId, int day){
        Task task;
        Cursor cursor = database.query(TABLE_NAME, null,
                COLUMN_WEEKDAY + " = '" + day + "' AND " + COLUMN_CHILD_ID + " = '" + childId + "'",  null, null,
                null, null, null);
        //
        Task taskDisabled = null;
        Task taskAccepted = null;
        while (cursor.moveToNext()) {
            task = getTask(cursor);
            if (task.isAccepted())
                taskAccepted = task;
            else
                taskDisabled = task;
        }

        cursor.close();
        if (taskAccepted != null && taskDisabled != null)
            return taskDisabled;
        else if (taskAccepted != null)
            return taskAccepted;
        else
            return taskDisabled;
    }

    public void delete(){
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        database.execSQL(clearDBQuery);
    }

    public ArrayList<Task> getEverything(int childId){
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_CHILD_ID + " = '" + childId + "'", null,
                null, null, null);
        while (cursor.moveToNext()) {
            tasks.add(getTask(cursor));
        }
        cursor.close();
        return tasks;
    }
}
