package com.zybooks.event_tracking_app_kondieieva;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int VERSION = 1;

    // constructor
    public TaskDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // db schema
    public static final class TaskTable {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_ID = "id";
        public static final String COL_USER_NAME = "user_name";
        public static final String COL_TASK_NAME = "task_name";
        public static final String COL_TASK_DESCRIPTION = "description";
        public static final String COL_TASK_DATE_TIME = "date_time";
        public static final String COL_COMPLETED = "completed";
        public static final String COL_REMINDER = "reminder";
    }


    // enable foreign key configuration to ensure task belongs to a valid user
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // create db
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TaskDatabase.TaskTable.TABLE + " (" +
                TaskTable.COL_TASK_ID + " integer primary key autoincrement," +
                TaskTable.COL_USER_NAME + " text not null," +
                TaskTable.COL_TASK_NAME + " text not null," +
                TaskTable.COL_TASK_DESCRIPTION + " text," +
                TaskTable.COL_TASK_DATE_TIME + " integer not null," +
                TaskTable.COL_COMPLETED + " integer not null default 0," +
                TaskTable.COL_REMINDER + " integer not null default 0," +
                // if user is deleted, all the tasks related to the username will be deleted
                "foreign key("+ TaskTable.COL_USER_NAME +") references users(username) on delete cascade)"
        );
    }

    // update if the version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TaskTable.TABLE);
        onCreate(db);
    }

}
