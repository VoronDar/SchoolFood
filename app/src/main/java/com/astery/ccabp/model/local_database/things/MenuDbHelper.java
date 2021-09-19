package com.astery.ccabp.model.local_database.things;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MenuDbHelper extends SQLiteOpenHelper {
    public static final String NAME = "menu.db";
    public static final int VERSION = 1;

    public MenuDbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MenuContract.MenuEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MenuContract.MenuEntry.DROP_TABLE);
        onCreate(db);
    }
}
