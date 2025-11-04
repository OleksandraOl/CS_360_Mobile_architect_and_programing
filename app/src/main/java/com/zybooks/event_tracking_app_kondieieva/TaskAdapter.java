package com.zybooks.event_tracking_app_kondieieva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // initialize two view types for date and item body
    private static final int TYPE_DATE = 0;
    private static final int TYPE_EVENT = 1;
    private final List<Task> tasks;
    private TaskController taskController;

    // constructor
    public TaskAdapter(List<Task> tasks, TaskController taskController) {
        this.tasks = tasks;
        this.taskController = taskController;
    }

    // viewholdder for the task
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        ImageButton editButton, deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            // UI elements
            title = itemView.findViewById(R.id.event_title);
            time = itemView.findViewById(R.id.event_time);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    // viewholder for the date header
    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView day, month, year;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            // UI elements
            day = itemView.findViewById(R.id.date_day);
            month = itemView.findViewById(R.id.date_month);
            year = itemView.findViewById(R.id.date_year);
        }
    }

    // check if the list item date or item body
    @Override
    public int getItemViewType(int position) {
        // show date if it's the first task
        if (position == 0) {
            return TYPE_DATE;
        }

        Calendar prevDate = Calendar.getInstance();
        prevDate.setTimeInMillis(tasks.get(position - 1).getDateTime());

        Calendar currDate = Calendar.getInstance();
        currDate.setTimeInMillis(tasks.get(position).getDateTime());

        // show date if the date differs from the previous one
        if (prevDate.get(Calendar.YEAR) != currDate.get(Calendar.YEAR) ||
                prevDate.get(Calendar.MONTH) != currDate.get(Calendar.MONTH) ||
                prevDate.get(Calendar.DAY_OF_MONTH) != currDate.get(Calendar.DAY_OF_MONTH)) {
            return TYPE_DATE;
        }

        // return event type otherwise
        return TYPE_EVENT;
    }

    // inflate layout
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_DATE) {
            View v = inflater.inflate(R.layout.event_date, parent, false);
            return new DateViewHolder(v);
        } else {
            View v = inflater.inflate(R.layout.event_item, parent, false);
            return new EventViewHolder(v);
        }
    }

    // populate UI elements with date/task info
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Task task = tasks.get(position);

        if (getItemViewType(position) == TYPE_DATE) {
            DateViewHolder dateHolder = (DateViewHolder) holder;
            int day = TaskDateTimeController.getDay(task.getDateTime());
            int month = TaskDateTimeController.getMonth(task.getDateTime());
            int year = TaskDateTimeController.getYear(task.getDateTime());

            dateHolder.day.setText(String.valueOf(day));
            dateHolder.month.setText(String.valueOf(month + 1));
            dateHolder.year.setText(String.valueOf(year));
        } else {
           EventViewHolder eventHolder = (EventViewHolder) holder;
           eventHolder.title.setText(task.getTitle());

           int hour = TaskDateTimeController.getHour(task.getDateTime());
           int minute = TaskDateTimeController.getMinute(task.getDateTime());
           eventHolder.time.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));

           eventHolder.editButton.setOnClickListener(v -> {
               int adapterPosition = eventHolder.getBindingAdapterPosition();
               if (adapterPosition != RecyclerView.NO_POSITION) {
                   Task taskEdit = tasks.get(adapterPosition);

                   // fragment manager to navigate
                   Bundle bundle = new Bundle();
                   bundle.putLong("taskId", taskEdit.getId());
                   bundle.putString("userName", taskEdit.getUserName());

                   NavController navController = Navigation.findNavController(v);
                   navController.navigate(R.id.action_homeFragment_to_addEditTaskFragment, bundle);
               }
           });
           eventHolder.deleteButton.setOnClickListener(v -> {
               int adapterPosition = eventHolder.getBindingAdapterPosition();
               if (adapterPosition != RecyclerView.NO_POSITION) {
                   Task taskDelete = tasks.get(adapterPosition);
                   taskController.deleteTask(taskDelete.getId(), taskDelete.getUserName());
                   tasks.remove(adapterPosition);
                   notifyItemRemoved(adapterPosition);
               }
           });
        }
    }

    // total number of elements in the adapter
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

}
