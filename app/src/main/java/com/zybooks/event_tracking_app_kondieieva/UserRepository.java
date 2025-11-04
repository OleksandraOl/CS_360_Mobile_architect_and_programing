package com.zybooks.event_tracking_app_kondieieva;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zybooks.event_tracking_app_kondieieva.UserDatabase.UserTable;

public class UserRepository {
    private final UserDatabase dbManager;

    // constructor
    public UserRepository(Context context) {
        dbManager = new UserDatabase(context);
    }

    // get writable version of db
    public SQLiteDatabase getWritable() {
        return dbManager.getWritableDatabase();
    }
    // get readable version of db
    public SQLiteDatabase getReadable() {
        return dbManager.getReadableDatabase();
    }


    // TODO: implement hashing
    // add user
    public boolean addUser(User user) {
        SQLiteDatabase db = getWritable();

        ContentValues values = new ContentValues();
        values.put(UserTable.COL_USERNAME, user.getUsername());
        values.put(UserTable.COL_PASSWORD, user.getPassword());
        values.put(UserTable.COL_FIRST_NAME, user.getFirstName());
        values.put(UserTable.COL_LAST_NAME, user.getLastName());
        values.put(UserTable.COL_PHONE_NUMBER, user.getPhoneNumber());

        long result = db.insert(UserTable.TABLE, null, values);
        db.close();

        return result != 0;
    }

    // check username/login pair
    public boolean checkUsernameLoginPair(String username, String password) {
        SQLiteDatabase db = getReadable();

        Cursor cursor = db.query(
            UserTable.TABLE,
            new String[]{UserTable.COL_USERNAME},
                UserTable.COL_USERNAME + " = ? and " + UserTable.COL_PASSWORD +  " = ?",
            new String[]{username, password},
            null, null, null
        );

        // check if any result was returned
        boolean validUser = cursor.moveToFirst();

        cursor.close();
        db.close();

        return validUser;
    }


    // check if the username already exists
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = getReadable();

        Cursor cursor = db.query(
            UserTable.TABLE,
            new String[]{UserTable.COL_USERNAME},
            UserTable.COL_USERNAME + " = ? ",
            new String[]{username},
            null, null, null
        );

        boolean usernameExists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return usernameExists;
    }


    // get user info by username
    public User getUserInfo(String username) {
        SQLiteDatabase db = getReadable();

        Cursor cursor = db.query(
                UserTable.TABLE,
                null,
                UserTable.COL_USERNAME + " = ? ",
                new String[]{username},
                null, null, null
        );

        User foundUser = null;
        if (cursor.moveToFirst()) {
            foundUser = new User(
                cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_USERNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_PHONE_NUMBER))
            );
        }

        cursor.close();
        db.close();

        return foundUser;
    }

}
