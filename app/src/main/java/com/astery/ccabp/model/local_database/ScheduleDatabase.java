package com.astery.ccabp.model.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.astery.ccabp.model.cloud_database.pogo.GotSchedule;
import com.astery.ccabp.model.local_database.things.ScheduleDbHelper;

import java.util.ArrayList;

import static com.astery.ccabp.model.local_database.things.ScheduleContract.ScheduleEntry.COLUMN_BREAKFAST;
import static com.astery.ccabp.model.local_database.things.ScheduleContract.ScheduleEntry.COLUMN_BREAKFAST_MAX;
import static com.astery.ccabp.model.local_database.things.ScheduleContract.ScheduleEntry.COLUMN_CLASS_ID;
import static com.astery.ccabp.model.local_database.things.ScheduleContract.ScheduleEntry.COLUMN_LUNCH;
import static com.astery.ccabp.model.local_database.things.ScheduleContract.ScheduleEntry.COLUMN_LUNCH_MAX;
import static com.astery.ccabp.model.local_database.things.ScheduleContract.ScheduleEntry.COLUMN_SNACK;
import static com.astery.ccabp.model.local_database.things.ScheduleContract.ScheduleEntry.COLUMN_SNACK_MAX;
import static com.astery.ccabp.model.local_database.things.ScheduleContract.ScheduleEntry.TABLE_NAME;

public class ScheduleDatabase {
    private SQLiteDatabase database;
    protected Context context;

    private static ScheduleDatabase cb;

    public static ScheduleDatabase getInstance(Context context){
        if (cb != null && cb.database.isOpen()){
            return cb;
        }
        return new ScheduleDatabase(context);
    }


    private ScheduleDatabase(Context context) {
        this.context = context;
        cb = this;
        open();
    }


    public void open(){
        SQLiteOpenHelper dbHelper = new ScheduleDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        if (database.isOpen())
            database.close();
    }
    private ContentValues fillContentValues(GotSchedule menu, int classId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CLASS_ID, classId);
        contentValues.put(COLUMN_BREAKFAST , menu.getBreakfast());
        contentValues.put(COLUMN_BREAKFAST_MAX , menu.getBreakfastMax());
        contentValues.put(COLUMN_LUNCH , menu.getLanch());
        contentValues.put(COLUMN_LUNCH_MAX , menu.getLanchMax());
        contentValues.put(COLUMN_SNACK , menu.getSnack10());
        contentValues.put(COLUMN_SNACK_MAX , menu.getSnack15());
        return contentValues;
    }

    private GotSchedule getSchedule(Cursor cursor){
        return new GotSchedule((cursor.getString(cursor.getColumnIndex(COLUMN_BREAKFAST))),
                (cursor.getString(cursor.getColumnIndex(COLUMN_BREAKFAST_MAX))),
                (cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH))),
                (cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH_MAX))),
                (cursor.getString(cursor.getColumnIndex(COLUMN_SNACK))),
                (cursor.getString(cursor.getColumnIndex(COLUMN_SNACK_MAX))));
    }



    public void update(GotSchedule menu, int classId){
        ContentValues  cv = fillContentValues(menu, classId);
        if (database.update(TABLE_NAME, cv,
                COLUMN_CLASS_ID + " = '" + classId + "'",  null) <= 0) {
            database.insert(TABLE_NAME, null, cv);
        }
    }


    public GotSchedule getSchedule(int classId){
        GotSchedule menu = null;
        Cursor cursor = database.query(TABLE_NAME, null,
                COLUMN_CLASS_ID + " = '" + classId + "'",  null, null,
                null, null, null);
        if (cursor.moveToNext()) {
            menu = getSchedule(cursor);
        }
        cursor.close();
        return menu;
    }

    public void delete(){
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        database.execSQL(clearDBQuery);
    }

    public ArrayList<GotSchedule> getEverything(){
        ArrayList<GotSchedule> children = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            children.add(getSchedule(cursor));
        }
        cursor.close();
        return children;
    }



}
