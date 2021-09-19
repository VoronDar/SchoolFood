package com.astery.ccabp.model.local_database.things;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ScheduleDbHelper extends SQLiteOpenHelper {
    public static final String NAME = "schedule.db";
    public static final int VERSION = 1;

    public ScheduleDbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScheduleContract.ScheduleEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ScheduleContract.ScheduleEntry.DROP_TABLE);
        onCreate(db);
    }
}
