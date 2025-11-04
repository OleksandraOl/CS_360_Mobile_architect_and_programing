package com.zybooks.event_tracking_app_kondieieva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private CalendarView calendarView;
    private RecyclerView earliestEventsView;
    private FloatingActionButton fabAddEvent;

    private TaskController taskController;
    private TaskAdapter taskAdapter;

    //current username
    private String username;

    @Override
    public void onResume() {
        super.onResume();
        loadEarliestTasks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate hole layout
        View root = inflater.inflate(R.layout.fragment_home,container,false);

        // get current user username
        username = UserPrefs.getUsername(requireContext());

        // UI elements
        calendarView = root.findViewById(R.id.home_calendar);
        earliestEventsView = root.findViewById(R.id.earliest_events_view);
        fabAddEvent = root.findViewById(R.id.fab_add_event);

        // initialize task controller
        taskController = new TaskController(getContext());

        setUpRecyclerView();

        // calendar date selection
        calendarView.setOnDateChangeListener((view, year, month, day) ->{
            // TODO: implement automatic date setup for the date chosen on calendar
        });

        // fab click to open add/edit event fragment
        fabAddEvent.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_homeFragment_to_addEditTaskFragment);
        });

        return root;
    }

    private void setUpRecyclerView() {
        // initialize adapter with empty list
        taskAdapter = new TaskAdapter(new ArrayList<>(), taskController);

        // set layout manager and adapter
        earliestEventsView.setLayoutManager(new LinearLayoutManager(getContext()));
        earliestEventsView.setAdapter(taskAdapter);
    }

    private void loadEarliestTasks() {
        // TODO: replace user with current user
        List<Task> earliestTasks = taskController.getEarliestTasks(username, 3);

        if (earliestTasks.isEmpty()) {
            // TODO: display 'no current tasks' instead
            earliestEventsView.setVisibility(View.GONE);
        } else {
            earliestEventsView.setVisibility(View.VISIBLE);
            // update adapters tasks
            taskAdapter.setTasks(earliestTasks);
        }
    }
}
