package com.astery.ccabp.model.local_database.things;

import android.provider.BaseColumns;

public class ChildContract{
    public static final class ChildEntry  extends Entry implements BaseColumns{

        public static final String TABLE_NAME = "child_table";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SECOND_NAME = "s_name";
        public static final String COLUMN_SCHOOL = "school";
        public static final String COLUMN_FREE = "isFree";
        public static final String COLUMN_PATRONYMIC = "patronymic";
        public static final String COLUMN_GRADE = "grade";
        public static final String COLUMN_SCHOOL_NAME = "school_name";
        public static final String COLUMN_GRADE_ID = "column_grade_id";
        public static final String COLUMN_HOME_ID = "column_home_id";
        public static final String COLUMN_IS_ACTIVATED = "column_activated";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_NAME + " " + TYPE_TEXT + ", " +
                COLUMN_SECOND_NAME + " " + TYPE_TEXT + ", " +
                COLUMN_SCHOOL + " " + TYPE_INTEGER + ", " +
                COLUMN_HOME_ID + " " + TYPE_INTEGER + ", " +
                COLUMN_GRADE + " " + TYPE_TEXT + ", " +
                COLUMN_SCHOOL_NAME + " " + TYPE_TEXT + ", " +
                COLUMN_PATRONYMIC + " " + TYPE_TEXT + ", " +
                COLUMN_FREE + " " + TYPE_INTEGER + ", " +
                COLUMN_IS_ACTIVATED + " " + TYPE_INTEGER + ", " +
                COLUMN_GRADE_ID + " " + TYPE_INTEGER +  ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
