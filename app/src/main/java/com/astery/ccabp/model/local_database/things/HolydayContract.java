package com.astery.ccabp.model.local_database.things;

import android.provider.BaseColumns;

public class HolydayContract {
    public static final class DbEntry  extends Entry implements BaseColumns{

        public static final String TABLE_NAME = "holyday_table";

        public static final String COLUMN_CHILD_ID = "childId";
        public static final String COLUMN_START_DAY = "start";
        public static final String COLUMN_END_DAY = "end";
        public static final String COLUMN_MONTH = "month";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + _ID + " " + TYPE_INTEGER + ", " +
                COLUMN_CHILD_ID + " " + TYPE_INTEGER + ", " +
                COLUMN_START_DAY + " " + TYPE_INTEGER + ", " +
                COLUMN_MONTH + " " + TYPE_INTEGER + ", " +
                COLUMN_END_DAY + " " + TYPE_INTEGER +  ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
