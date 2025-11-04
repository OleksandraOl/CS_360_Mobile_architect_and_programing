package com.zybooks.event_tracking_app_kondieieva;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class TaskController {
    private final TaskRepository repository;

    public TaskController(Context context) {
        repository = new TaskRepository(context);
    }

    // clean the input information
    private String processTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }

        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        return title.trim();
    }

    private String processDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return null;
        }

        return description.trim();
    }

    // check validity
    private boolean isValidTitle(String title) {
        if (title.length() > 50) {
            throw new IllegalArgumentException("Title cannot be longer than 50 characters");
        }

        return true;
    }

    private boolean isValidDescription(String description) {
        if (description.length() > 200) {
            throw new IllegalArgumentException("Description cannot be longer than 200 characters");
        }

        return true;
    }



    // add new task
    public long addTask(Task task) {
        String cleanedTitle = processTitle(task.getTitle());
        String cleanedDescription = processDescription(task.getDescription());

        isValidTitle(cleanedTitle);
        if (cleanedDescription != null) {
            isValidDescription(cleanedDescription);
        }

        task.setTitle(cleanedTitle);
        task.setDescription(cleanedDescription);

        return repository.addTask(task);

    }

    // update task
    public boolean updateTask(Task task) {
        String cleanedTitle = processTitle(task.getTitle());
        String cleanedDescription = processDescription(task.getDescription());

        isValidTitle(cleanedTitle);
        if (cleanedDescription != null) {
            isValidDescription(cleanedDescription);
        }

        task.setTitle(cleanedTitle);
        task.setDescription(cleanedDescription);

        return repository.updateTask(task);
    }

    // delete task
    public boolean deleteTask(long taskId, String username) {
        return repository.deleteTask(taskId, username);
    }

    // find task by id
    public Task getTaskById(long taskId) {
        return repository.getTaskById(taskId);
    }

    // find all tasks for the user
    public List<Task> getAllTasks(String username) {
        return repository.getAllTasks(username);
    }

    // find tasks for one day
    public List<Task> getTasksForCurrentDay(long currentDate, String username) {
        // calendar to calculate the start and the end of the day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDate);

        // start of the day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();

        // end of the day
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long end = calendar.getTimeInMillis();

        return repository.getTasksBetweenDates(start, end, username);
    }

    // find tasks between dates
    public List<Task> getTasksBetweenDates(long startDate, long endDate, String username) {
        return repository.getTasksBetweenDates(startDate, endDate, username);
    }

    // find tasks between date
    public List<Task> getTasksForWeek(long currentDate, String username) {
        // calendar to calculate the start and the end of the day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDate);

        // start of the week
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfWeek = calendar.getTimeInMillis();

        // end of the week
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DAY_OF_WEEK,6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endOfWeek = calendar.getTimeInMillis();

        return repository.getTasksBetweenDates(startOfWeek, endOfWeek, username);
    }

    // get earliest n upcoming tasks
    public List<Task> getEarliestTasks(String username, int limit) {
        List<Task> allTasks = repository.getAllTasks(username);

        // filter tasks that are in the future and not completed yet
        long now = System.currentTimeMillis();
        List<Task> upcomingTasks = new ArrayList<>();

        for(Task task: allTasks) {
            if(!task.isCompleted() && task.getDateTime() >= now) {
                upcomingTasks.add(task);
            }
        }

        // sort by date
        upcomingTasks.sort(Comparator.comparingLong(Task::getDateTime));

        // return the required number of tasks
        return upcomingTasks.size() <= limit ? upcomingTasks : upcomingTasks.subList(0, limit);
    }

}
