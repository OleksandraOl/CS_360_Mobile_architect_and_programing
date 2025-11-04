package com.zybooks.event_tracking_app_kondieieva;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class AuthFragment extends Fragment {
    private UserController userController;
    private static final String KEY_LOGIN_VISIBILITY = "loginVisible";
    private static final String KEY_SIGN_UP_VISIBILITY = "signUpVisible";

    // UI elements
    private LinearLayout loginLayout, signUpLayout;

    // login
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView switchToSignUpLink;

    // sign up
    private EditText signUpFirstName, signUpLastName, signUpPhone, signUpEmail,
            signUpPassword, signUpConfirmPassword;
    private Button signUpButton;
    private TextView switchToLoginLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_sign_up, container, false);

        // initialize controller
        userController = new UserController(getContext());

        // login elements
        loginLayout = root.findViewById(R.id.login_layout);
        loginEmail = root.findViewById(R.id.login_email);
        loginPassword = root.findViewById(R.id.login_password);
        loginButton = root.findViewById(R.id.login_button);
        switchToSignUpLink = root.findViewById(R.id.switch_to_sign_up);

        // sign up elements
        signUpLayout = root.findViewById(R.id.sign_up_layout);
        signUpFirstName = root.findViewById(R.id.sign_up_first_name);
        signUpLastName = root.findViewById(R.id.sign_up_last_name);
        signUpPhone = root.findViewById(R.id.sign_up_phone_number);
        signUpEmail = root.findViewById(R.id.sign_up_email);
        signUpPassword = root.findViewById(R.id.sign_up_password);
        signUpConfirmPassword = root.findViewById(R.id.sign_up_confirm_password);
        signUpButton = root.findViewById(R.id.sign_up_button);
        switchToLoginLink = root.findViewById(R.id.switch_to_login);

        // listeners
        loginButton.setOnClickListener(v -> manageLogin());
        signUpButton.setOnClickListener(v -> manageSignUp());

        // manage links to switch between login and sign up
        switchToSignUpLink.setOnClickListener(v -> {
            loginLayout.setVisibility(View.GONE);
            signUpLayout.setVisibility(View.VISIBLE);
        });

        switchToLoginLink.setOnClickListener(v -> {
            signUpLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        });

        // TODO: implement text watcher to improve user experience
        return root;
    }

    private void manageLogin() {
        // TODO: limit number of login/password inputs
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        try {
            boolean isMatch = userController.checkUsernameLoginPair(email, password);
            if (isMatch) {
                // save username in shared preferences
                UserPrefs.setUsername(requireContext(), email);

                Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_SHORT).show();

                // TODO: remember user after a login
                // skip permission if was already granted
                if (UserPrefs.getSmsPermission(requireContext())) {
                    // granted proceed to home
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.action_authFragment_to_homeFragment);
                    //NavHostFragment.findNavController(this).navigate(R.id.action_authFragment_to_homeFragment);
                } else {
                    // request permission
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.action_authFragment_to_permissionRequestFragment);
                    //NavHostFragment.findNavController(this).navigate(R.id.action_authFragment_to_permissionRequestFragment);
                }
            } else {
                Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();

            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void manageSignUp() {
        String firstName = signUpFirstName.getText().toString();
        String lastName = signUpLastName.getText().toString();
        String phoneNumber = signUpPhone.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();
        String confirmPassword = signUpConfirmPassword.getText().toString();

        try {
            userController.areTwoPasswordsSame(password, confirmPassword);
            User newUser = new User(email, password, firstName, lastName, phoneNumber);
            userController.addUser(newUser);
            Toast.makeText(getContext(), "Sign up successful!", Toast.LENGTH_SHORT).show();

            // switch to login after successful sign up
            signUpLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // save auth fragment state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LOGIN_VISIBILITY, loginLayout.getVisibility() == View.VISIBLE);
        outState.putBoolean(KEY_SIGN_UP_VISIBILITY, signUpLayout.getVisibility() == View.VISIBLE);
    }

    // restore fragment state
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            loginLayout.setVisibility(savedInstanceState.getBoolean(KEY_LOGIN_VISIBILITY)
                    ? View.VISIBLE : View.GONE);
            signUpLayout.setVisibility(savedInstanceState.getBoolean(KEY_SIGN_UP_VISIBILITY)
                    ? View.VISIBLE : View.GONE);
        }
    }

}
