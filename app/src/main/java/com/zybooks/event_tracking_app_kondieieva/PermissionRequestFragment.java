package com.zybooks.event_tracking_app_kondieieva;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class PermissionRequestFragment extends Fragment {
    private Button allowButton, denyButton;

    // permission request launcher
    private final ActivityResultLauncher<String> requestPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted) {
                    UserPrefs.setSmsPermission(requireContext(), true);
                } else {
                    UserPrefs.setSmsPermission(requireContext(), false);
                }

                // navigate to the home screen
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.action_permissionRequestFragment_to_homeFragment);
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sms_permission, container, false);

        // UI elements
        allowButton = root.findViewById(R.id.allow_sms_button);
        denyButton = root.findViewById(R.id.deny_sms_button);

        allowButton.setOnClickListener(
                v -> requestPermission.launch(Manifest.permission.SEND_SMS)
        );

        denyButton.setOnClickListener( v -> {
            UserPrefs.setSmsPermission(requireContext(), false);
            NavHostFragment.findNavController(this).navigate(R.id.action_permissionRequestFragment_to_homeFragment);
        });

        return root;
    }

}
