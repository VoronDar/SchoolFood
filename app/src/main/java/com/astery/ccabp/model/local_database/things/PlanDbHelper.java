package com.astery.ccabp.model.local_database.things;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PlanDbHelper extends SQLiteOpenHelper {
    public static final String NAME = "plan.db";
    public static final int VERSION = 1;

    public PlanDbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PlanContract.PlanEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PlanContract.PlanEntry.DROP_TABLE);
        onCreate(db);
    }
}
