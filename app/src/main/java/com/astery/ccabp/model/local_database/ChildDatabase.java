package com.astery.ccabp.model.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.astery.ccabp.model.local_database.things.ChildDbHelper;
import com.astery.ccabp.model.things.Child;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_FREE;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_GRADE;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_GRADE_ID;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_HOME_ID;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_IS_ACTIVATED;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_NAME;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_PATRONYMIC;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_SCHOOL;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_SCHOOL_NAME;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.COLUMN_SECOND_NAME;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.TABLE_NAME;

public class ChildDatabase {
    private SQLiteDatabase database;
    protected Context context;

    private static ChildDatabase cb;

    public static ChildDatabase getInstance(Context context){
        if (cb != null && cb.database.isOpen()){
            return cb;
        }
        return new ChildDatabase(context);
    }


    private ChildDatabase(Context context) {
        this.context = context;
        cb = this;
        open();
    }


    public void open(){
        SQLiteOpenHelper dbHelper = new ChildDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        if (database.isOpen())
            database.close();
    }
    private ContentValues fillContentValues(Child child){
        ContentValues contentValues = new ContentValues();
        if (child.getId() != null)
            contentValues.put(_ID, child.getId());
        contentValues.put(COLUMN_NAME, child.getName());
        contentValues.put(COLUMN_SECOND_NAME, child.getsName());
        contentValues.put(COLUMN_PATRONYMIC, child.getpName());
        contentValues.put(COLUMN_FREE, child.isFree());
        contentValues.put(COLUMN_SCHOOL, child.getSchool());
        contentValues.put(COLUMN_HOME_ID, child.getHomeId());
        contentValues.put(COLUMN_SCHOOL_NAME, child.getSchoolName());
        contentValues.put(COLUMN_GRADE, child.getGrade());
        contentValues.put(COLUMN_GRADE_ID, child.getClassId());
        contentValues.put(COLUMN_IS_ACTIVATED, child.isActivated());
        return contentValues;
    }

    private Child getChild(Cursor cursor){
        return new Child(cursor.getString(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_SECOND_NAME)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_SCHOOL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_GRADE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_GRADE_ID)),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ACTIVATED))>0),
                (cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOL_NAME))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_HOME_ID))),
                (cursor.getString(cursor.getColumnIndex(COLUMN_PATRONYMIC))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_FREE))>0)
                );
    }



    public void update(Child child){
        ContentValues  cv = fillContentValues(child);
        if (database.update(TABLE_NAME, cv,
                _ID + " == ?", new String[]{child.getId()}) <= 0) {
            database.insert(TABLE_NAME, null, cv);
        }
    }

    public void reUpdate(Child child, String last){
        delete(child.getId());
        child.setId(last);
        update(child);
    }

    public void delete(String id){
        String where = _ID + " = ?";
        String[] whereArgs = new String[]{id};
        database.delete(TABLE_NAME, where, whereArgs);
    }

    public ArrayList<Child> getEverything(){
        ArrayList<Child> children = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            children.add(getChild(cursor));
        }
        cursor.close();
        return children;
    }


}
