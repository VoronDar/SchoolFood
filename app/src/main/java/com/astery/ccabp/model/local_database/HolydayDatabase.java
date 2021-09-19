package com.astery.ccabp.model.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.astery.ccabp.model.cloud_database.pogo.GotHolyday;
import com.astery.ccabp.model.local_database.things.HolydayDbHelper;

import java.util.ArrayList;

import static com.astery.ccabp.model.local_database.things.HolydayContract.DbEntry.COLUMN_CHILD_ID;
import static com.astery.ccabp.model.local_database.things.HolydayContract.DbEntry.COLUMN_END_DAY;
import static com.astery.ccabp.model.local_database.things.HolydayContract.DbEntry.COLUMN_MONTH;
import static com.astery.ccabp.model.local_database.things.HolydayContract.DbEntry.COLUMN_START_DAY;
import static com.astery.ccabp.model.local_database.things.HolydayContract.DbEntry.TABLE_NAME;

public class HolydayDatabase {
    private SQLiteDatabase database;
    protected Context context;

    private static HolydayDatabase cb;

    public static HolydayDatabase getInstance(Context context){
        if (cb != null && cb.database.isOpen()){
            return cb;
        }
        return new HolydayDatabase(context);
    }


    private HolydayDatabase(Context context) {
        this.context = context;
        cb = this;
        open();
    }


    public void open(){
        SQLiteOpenHelper dbHelper = new HolydayDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        if (database.isOpen())
            database.close();
    }
    private ContentValues fillContentValues(GotHolyday holyday){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CHILD_ID, holyday.getChildId());
        contentValues.put(COLUMN_START_DAY , holyday.getStart());
        contentValues.put(COLUMN_MONTH , holyday.getMonth());
        contentValues.put(COLUMN_END_DAY , holyday.getEnd());
        return contentValues;
    }

    private GotHolyday getSchedule(Cursor cursor){
        return new GotHolyday((cursor.getInt(cursor.getColumnIndex(COLUMN_CHILD_ID))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_START_DAY))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_END_DAY))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_MONTH))));
    }

    public void update(GotHolyday gotHolyday){
        ContentValues  cv = fillContentValues(gotHolyday);
        if (database.update(TABLE_NAME, cv,
                COLUMN_MONTH + " = '" + gotHolyday.getMonth() + "' AND " + COLUMN_CHILD_ID + " = '" + gotHolyday.getChildId() +
                        "' AND " + COLUMN_START_DAY + " = '" + gotHolyday.getStart() +  "' AND " + COLUMN_END_DAY + " = '" + gotHolyday.getEnd() + "'",  null) <= 0) {
            database.insert(TABLE_NAME, null, cv);
        }
    }

    public ArrayList<GotHolyday> getHolyday(int childId, int month){
        ArrayList<GotHolyday> holydays = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null,
                COLUMN_MONTH + " = '" + month + "' AND " + COLUMN_CHILD_ID + " = '" + childId + "'",  null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            holydays.add(getSchedule(cursor));
        }

        cursor.close();
        return holydays;
    }

    public void delete(){
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        database.execSQL(clearDBQuery);
    }

    public void deleteOld(int now){
        database.delete(TABLE_NAME, COLUMN_MONTH + " < '" + now + "'", null);

    }



}
