package com.astery.ccabp.model.local_database.things;

import android.provider.BaseColumns;

public class PlanContract {
    public static final class PlanEntry  extends Entry implements BaseColumns{

        public static final String TABLE_NAME = "plan_table";

        public static final String COLUMN_CHILD_ID = "childId";
        public static final String COLUMN_WEEKDAY = "day";
        public static final String COLUMN_BREAKFAST = "br";
        public static final String COLUMN_BREAKFAST_MAX = "brm";
        public static final String COLUMN_LUNCH = "l";
        public static final String COLUMN_LUNCH_MAX = "lm";
        public static final String COLUMN_SNACK = "s";
        public static final String COLUMN_SNACK_MAX = "sm";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + _ID + " " + TYPE_INTEGER + ", " +
                COLUMN_CHILD_ID + " " + TYPE_INTEGER + ", " +
                COLUMN_WEEKDAY + " " + TYPE_INTEGER + ", " +
                COLUMN_BREAKFAST + " " + TYPE_INTEGER + ", " +
                COLUMN_BREAKFAST_MAX + " " + TYPE_INTEGER + ", " +
                COLUMN_LUNCH + " " + TYPE_INTEGER + ", " +
                COLUMN_LUNCH_MAX + " " + TYPE_INTEGER + ", " +
                COLUMN_SNACK + " " + TYPE_INTEGER + ", " +
                COLUMN_SNACK_MAX + " " + TYPE_INTEGER +  ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
