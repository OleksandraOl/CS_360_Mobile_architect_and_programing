package com.zybooks.event_tracking_app_kondieieva;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int VERSION = 1;

    // constructor
    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // db schema
    public static final class UserTable {
        public static final String TABLE = "users";
        // email
        public static final String COL_USERNAME = "username";
        public static final String COL_PASSWORD = "password";
        public static final String COL_FIRST_NAME = "first_name";
        public static final String COL_LAST_NAME = "last_name";
        public static final String COL_PHONE_NUMBER = "phone_number";
    }

    // create db
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserTable.TABLE + " (" +
                UserTable.COL_USERNAME + " text primary key," +
                UserTable.COL_PASSWORD + " text not null," +
                UserTable.COL_FIRST_NAME + " text not null," +
                UserTable.COL_LAST_NAME + " text not null," +
                UserTable.COL_PHONE_NUMBER + " text )"
        );
    }

    // update if the version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserTable.TABLE);
        onCreate(db);
    }
}
