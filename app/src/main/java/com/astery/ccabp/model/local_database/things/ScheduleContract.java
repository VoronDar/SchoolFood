package com.astery.ccabp.model.local_database.things;

import android.provider.BaseColumns;

public class ScheduleContract {
    public static final class ScheduleEntry extends Entry implements BaseColumns{

        public static final String TABLE_NAME = "schedule_table";

        public static final String COLUMN_CLASS_ID = "class";
        public static final String COLUMN_BREAKFAST = "br";
        public static final String COLUMN_BREAKFAST_MAX = "brm";
        public static final String COLUMN_LUNCH = "l";
        public static final String COLUMN_LUNCH_MAX = "lm";
        public static final String COLUMN_SNACK = "s";
        public static final String COLUMN_SNACK_MAX = "sm";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + _ID + " " + TYPE_INTEGER + ", " +
                COLUMN_CLASS_ID + " " + TYPE_INTEGER + ", " +
                COLUMN_BREAKFAST + " " + TYPE_TEXT + ", " +
                COLUMN_BREAKFAST_MAX + " " + TYPE_TEXT + ", " +
                COLUMN_LUNCH + " " + TYPE_TEXT + ", " +
                COLUMN_LUNCH_MAX + " " + TYPE_TEXT + ", " +
                COLUMN_SNACK + " " + TYPE_TEXT + ", " +
                COLUMN_SNACK_MAX + " " + TYPE_TEXT +  ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
