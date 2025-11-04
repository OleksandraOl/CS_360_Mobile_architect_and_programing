package com.zybooks.event_tracking_app_kondieieva;

public class Task {
    private long id;
    private final String userName;
    private String title;
    private String description;
    private long dateTime;

    // TODO: implement 'completed' field and logic for completed tasks
    private boolean isCompleted;
    private boolean hasReminder;
    // 24 hours before the set date
    private long reminderTime;

    // constructor
    public Task(long id, String userName, String title, String description, long dateTime, boolean isCompleted,
                boolean hasReminder) {
        this.id = id;       // 0 before insert
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.isCompleted = isCompleted;
        this.hasReminder = hasReminder;
        // the app will notify 24 hours before the date
        this.reminderTime = dateTime - 24 * 60 * 60 * 1000;

    }

    // setters
    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    // if due time changes the reminder date needs to be recalculated
    public void setDateTime(long newDateTime) {
        this.dateTime = newDateTime;
        this.reminderTime = dateTime - 24 * 60 * 60 * 1000;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }


    // getters
    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getDateTime() {
        return dateTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean hasReminder() {
        return hasReminder;
    }

    public long getReminderTime() {
        return reminderTime;
    }

}
