package com.zybooks.event_tracking_app_kondieieva;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;

public class AddEditTaskFragment extends Fragment {
    private TextInputEditText titleInput, descriptionInput, dateInput, timeInput;
    private RadioGroup reminderGroup;
    private Button saveButton, cancelButton;

    private TaskController taskController;
    private Task existingTask;       //null if new task
    private String username;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.add_event, container, false);

        // UI elements
        titleInput = root.findViewById(R.id.add_event_title);
        descriptionInput = root.findViewById(R.id.add_event_description);
        dateInput = root.findViewById(R.id.add_event_date);
        timeInput = root.findViewById(R.id.add_event_time);
        reminderGroup = root.findViewById(R.id.radio_buttons_reminder);
        saveButton = root.findViewById(R.id.save_button);
        cancelButton = root.findViewById(R.id.cancel_button);

        // initialize controller and username
        taskController = new TaskController(getContext());
        username = UserPrefs.getUsername(requireContext());

        // date/time picker
        setUpDateTimePicker();

        // check if editing
        if (getArguments() != null && getArguments().containsKey("taskId")) {
            long taskId = getArguments().getLong("taskId");
            existingTask = taskController.getTaskById(taskId);
            preFillFields();
        }

        //save task
        saveButton.setOnClickListener(v -> saveTask());
        // cancel action
        cancelButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigateUp();
        });

        return root;
    }


    private void setUpDateTimePicker() {
        dateInput.setOnClickListener( v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dialog= new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) ->
                            dateInput.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        timeInput.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog dialog = new TimePickerDialog(requireContext(),
                    (view, hourOfDay, minute) ->
                            timeInput.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
            dialog.show();
        });
    }

    private void preFillFields() {
        // check if the task exists
        if (existingTask == null) {
            return;
        }

        titleInput.setText(existingTask.getTitle());
        descriptionInput.setText(existingTask.getDescription());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(existingTask.getDateTime());

        dateInput.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        timeInput.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        if (existingTask.hasReminder()) {
            reminderGroup.check(R.id.radio_yes);
        } else {
            reminderGroup.check(R.id.radio_no);
        }
    }

    private void saveTask() {
        try {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText() != null ? descriptionInput.getText().toString().trim() : null;
            boolean hasReminder = reminderGroup.getCheckedRadioButtonId() == R.id.radio_yes;

            //parse date and time
            String[] dateParts = dateInput.getText().toString().split("/");
            String[] timeParts = timeInput.getText().toString().split(":");

            long dateTime = TaskDateTimeController.toMilliSec(
                    // year
                    Integer.parseInt(dateParts[2]),
                    // month
                    Integer.parseInt(dateParts[1]) - 1,
                    // day
                    Integer.parseInt(dateParts[0]),
                    // hour
                    Integer.parseInt(timeParts[0]),
                    // minute
                    Integer.parseInt(timeParts[1])
            );

            if (existingTask == null) {
                Task task = new Task(0, username, title, description, dateTime, false, hasReminder);
                taskController.addTask(task);
            } else {
                existingTask.setTitle(title);
                existingTask.setDescription(description);
                existingTask.setDateTime(dateTime);
                existingTask.setReminder(hasReminder);
                taskController.updateTask(existingTask);
            }

            Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).popBackStack();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
