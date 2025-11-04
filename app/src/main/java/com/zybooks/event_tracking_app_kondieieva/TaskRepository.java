package com.zybooks.event_tracking_app_kondieieva;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.zybooks.event_tracking_app_kondieieva.TaskDatabase.TaskTable;

import java.util.ArrayList;
import java.util.List;


public class TaskRepository {

    // db instance to handle all operations
    private final TaskDatabase dbManager;

    // constructor
    public TaskRepository(Context context) {
        dbManager = new TaskDatabase(context);
    }

    // get writable version of db
    public SQLiteDatabase getWritable() {
        return dbManager.getWritableDatabase();
    }
    // get readable version of db
    public SQLiteDatabase getReadable() {
        return dbManager.getReadableDatabase();
    }

    // add task
    public long addTask(Task task) {
        SQLiteDatabase db = getWritable();

        ContentValues values = new ContentValues();
        values.put(TaskTable.COL_USER_NAME, task.getUserName());
        values.put(TaskTable.COL_TASK_NAME, task.getTitle());
        values.put(TaskTable.COL_TASK_DESCRIPTION, task.getDescription());
        values.put(TaskTable.COL_TASK_DATE_TIME, task.getDateTime());
        values.put(TaskTable.COL_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(TaskTable.COL_REMINDER, task.hasReminder() ? 1 : 0);

        long taskId = db.insert(TaskTable.TABLE, null, values);
        db.close();

        // update task with the generated if by the db
        task.setId(taskId);

        return taskId;
    }

    // update task
    public boolean updateTask(Task task) {
        SQLiteDatabase db = getWritable();

        ContentValues values = new ContentValues();
        values.put(TaskTable.COL_TASK_NAME, task.getTitle());
        values.put(TaskTable.COL_TASK_DESCRIPTION, task.getDescription());
        values.put(TaskTable.COL_TASK_DATE_TIME, task.getDateTime());
        values.put(TaskTable.COL_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(TaskTable.COL_REMINDER, task.hasReminder() ? 1 : 0);

        int rowsUpdated = db.update(
                TaskTable.TABLE,
                values,
                TaskTable.COL_TASK_ID + " = ? ",
                new String[]{String.valueOf(task.getId())}
        );
        db.close();

        return rowsUpdated > 0;
    }

    // delete task
    public boolean deleteTask(long taskId, String username) {
        SQLiteDatabase db = getWritable();

        int rowsDeleted = db.delete(
                TaskTable.TABLE,
                TaskTable.COL_TASK_ID + " = ? and " + TaskTable.COL_USER_NAME + " = ?",
                new String[]{String.valueOf(taskId), username}
        );
        db.close();

        return rowsDeleted > 0;
    }

    // find task by id
    public Task getTaskById(long taskId) {
        SQLiteDatabase db = getReadable();

        Cursor cursor = db.query(
                TaskTable.TABLE,
                null,
                TaskTable.COL_TASK_ID + " = ? ",
                new String[]{String.valueOf(taskId)},
                null, null, null
        );

        Task foundTask = null;
        if (cursor.moveToFirst()) {
            foundTask = new Task(
                cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_DESCRIPTION)),
                cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_DATE_TIME)),
     cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_COMPLETED)) != 0,
     cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_REMINDER)) != 0
            );
        }

        cursor.close();
        db.close();

        return foundTask;
    }

    // find all tasks of the user
    public List<Task> getAllTasks(String username) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadable();

        String selection = TaskTable.COL_USER_NAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                TaskTable.TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                TaskTable.COL_TASK_DATE_TIME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Task foundTask = new Task(
                        cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_USER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_DESCRIPTION)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_DATE_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_COMPLETED)) != 0,
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_REMINDER)) != 0
                );
                taskList.add(foundTask);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return taskList;
    }


    // get tasks by date
    public List<Task> getTasksBetweenDates(long startTime, long endTime, String username) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadable();

        String selection = TaskTable.COL_USER_NAME + " = ? AND " + TaskTable.COL_TASK_DATE_TIME + " BETWEEN ? AND ?";
        String[] selectionArgs = {
                username,
                String.valueOf(startTime),
                String.valueOf(endTime)
        };

        Cursor cursor = db.query(
                TaskTable.TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                TaskTable.COL_TASK_DATE_TIME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Task foundTask = new Task(
                        cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_USER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_DESCRIPTION)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_TASK_DATE_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_COMPLETED)) != 0,
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_REMINDER)) != 0
                );
                taskList.add(foundTask);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return taskList;
    }

}
