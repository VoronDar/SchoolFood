package com.astery.ccabp.model.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.astery.ccabp.model.cloud_database.pogo.PostMenu;
import com.astery.ccabp.model.local_database.things.ChildContract;
import com.astery.ccabp.model.local_database.things.MenuDbHelper;
import com.astery.ccabp.model.things.Child;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_BREAKFAST;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_BREAKFAST_MAX;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_HOME_ID;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_LUNCH;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_LUNCH_MAX;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_SNACK;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_SNACK_MAX;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_WEEK;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_WEEKDAY;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.COLUMN_YOUNG;
import static com.astery.ccabp.model.local_database.things.MenuContract.MenuEntry.TABLE_NAME;

public class MenuDatabase {
    private SQLiteDatabase database;
    protected Context context;

    private static MenuDatabase cb;

    public static MenuDatabase getInstance(Context context){
        if (cb != null && cb.database.isOpen()){
            return cb;
        }
        return new MenuDatabase(context);
    }


    private MenuDatabase(Context context) {
        this.context = context;
        cb = this;
        open();
    }


    public void open(){
        SQLiteOpenHelper dbHelper = new MenuDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        if (database.isOpen())
            database.close();
    }
    private ContentValues fillContentValues(PostMenu menu){
        ContentValues contentValues = new ContentValues();
        if (menu.getId() != -1)
            contentValues.put(_ID, menu.getId());
        contentValues.put(COLUMN_HOME_ID, menu.getHomeId());
        contentValues.put(COLUMN_WEEK, menu.getWeek());
        contentValues.put(COLUMN_WEEKDAY, menu.getWeekday());
        contentValues.put(COLUMN_YOUNG, menu.isYoung());
        contentValues.put(COLUMN_BREAKFAST , makeString(menu.getBreakfast()));
        contentValues.put(COLUMN_BREAKFAST_MAX , makeString(menu.getBreakfastMax()));
        contentValues.put(COLUMN_LUNCH , makeString(menu.getLanch()));
        contentValues.put(COLUMN_LUNCH_MAX , makeString(menu.getLanchMax()));
        contentValues.put(COLUMN_SNACK , makeString(menu.getSnack10()));
        contentValues.put(COLUMN_SNACK_MAX , makeString(menu.getSnack15()));
        return contentValues;
    }

    private PostMenu getMenu(Cursor cursor){
        return new PostMenu(cursor.getInt(cursor.getColumnIndex(_ID)),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_HOME_ID))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_WEEKDAY))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_WEEK))),
                (cursor.getInt(cursor.getColumnIndex(COLUMN_YOUNG)))>0,
                makeList(cursor.getString(cursor.getColumnIndex(COLUMN_BREAKFAST))),
                makeList(cursor.getString(cursor.getColumnIndex(COLUMN_BREAKFAST_MAX))),
                makeList(cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH))),
                makeList(cursor.getString(cursor.getColumnIndex(COLUMN_LUNCH_MAX))),
                makeList(cursor.getString(cursor.getColumnIndex(COLUMN_SNACK))),
                makeList(cursor.getString(cursor.getColumnIndex(COLUMN_SNACK_MAX))));
    }



    public void update(PostMenu menu){
        ContentValues  cv = fillContentValues(menu);
        if (database.update(TABLE_NAME, cv,
                COLUMN_WEEK + " = '" + menu.getWeek() + "' AND " + COLUMN_WEEKDAY + " = '" + menu.getWeekday()
                        + "' AND " + COLUMN_HOME_ID + " = '" + menu.getHomeId() + "'",  null) <= 0) {
            database.insert(TABLE_NAME, null, cv);
        }
    }


    public PostMenu getMenuToday(int week, int weekday, int homeId, boolean isYoung){
        PostMenu menu = null;
        Cursor cursor = database.query(TABLE_NAME, null,
                COLUMN_WEEK + " = '" + week + "' AND " + COLUMN_WEEKDAY + " = '" + weekday
                        + "' AND " + COLUMN_HOME_ID + " = '" + homeId + "'"
                , null,
                null, null, null);
        if (cursor.moveToNext()) {
            menu = getMenu(cursor);
        }
        cursor.close();
        return menu;
    }

    public void delete(){
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        database.execSQL(clearDBQuery);
    }

    public ArrayList<PostMenu> getEverything(){
        ArrayList<PostMenu> children = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            children.add(getMenu(cursor));
        }
        cursor.close();
        return children;
    }

    private String makeString(List<String> str){
        if (str == null || str.size() == 0) return "";

        StringBuilder builder = new StringBuilder();
        for (String st: str){
            builder.append(st).append("|");
        }
        return builder.toString().substring(0, builder.length()-1);
    }

    private List<String> makeList(String str){
        List<String> l = new ArrayList<>();
        if (str == null || str.length() < 3) return l;

        String[] ar = str.split("\\|");

        l.addAll(Arrays.asList(ar));
        return l;
    }


}
