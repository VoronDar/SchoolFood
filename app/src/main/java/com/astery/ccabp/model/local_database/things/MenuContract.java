package com.astery.ccabp.model.local_database.things;

import android.provider.BaseColumns;

public class MenuContract {
    public static final class MenuEntry  extends Entry implements BaseColumns{

        public static final String TABLE_NAME = "menu_table";

        public static final String COLUMN_HOME_ID = "home";
        public static final String COLUMN_YOUNG = "young";
        public static final String COLUMN_WEEKDAY = "day";
        public static final String COLUMN_WEEK = "week";
        public static final String COLUMN_BREAKFAST = "br";
        public static final String COLUMN_BREAKFAST_MAX = "brm";
        public static final String COLUMN_LUNCH = "l";
        public static final String COLUMN_LUNCH_MAX = "lm";
        public static final String COLUMN_SNACK = "s";
        public static final String COLUMN_SNACK_MAX = "sm";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + _ID + " " + TYPE_INTEGER + ", " +
                COLUMN_YOUNG + " " + TYPE_INTEGER + ", " +
                COLUMN_HOME_ID + " " + TYPE_INTEGER + ", " +
                COLUMN_WEEKDAY + " " + TYPE_INTEGER + ", " +
                COLUMN_WEEK + " " + TYPE_INTEGER + ", " +
                COLUMN_BREAKFAST + " " + TYPE_TEXT + ", " +
                COLUMN_BREAKFAST_MAX + " " + TYPE_TEXT + ", " +
                COLUMN_LUNCH + " " + TYPE_TEXT + ", " +
                COLUMN_LUNCH_MAX + " " + TYPE_TEXT + ", " +
                COLUMN_SNACK + " " + TYPE_TEXT + ", " +
                COLUMN_SNACK_MAX + " " + TYPE_TEXT +  ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
