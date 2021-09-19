package com.astery.ccabp.model.local_database.things;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HolydayDbHelper extends SQLiteOpenHelper {
    public static final String NAME = "holyday.db";
    public static final int VERSION = 1;

    public HolydayDbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HolydayContract.DbEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(HolydayContract.DbEntry.DROP_TABLE);
        onCreate(db);
    }
}
