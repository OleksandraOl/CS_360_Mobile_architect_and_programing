package com.zybooks.event_tracking_app_kondieieva;

import android.content.Context;
import android.util.Patterns;

public class UserController {
    private final UserRepository repository;

    public UserController(Context context) {
        repository = new UserRepository(context);
    }


    // clean the input information
    private String processEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        return email.trim().toLowerCase();
    }

    // TODO: enhance password security by requiring at least one capital letter, number and special symbol
    private String processPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        password = password.replace(" ", "");
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password needs to be at least 8 characters long");
        }

        return password;
    }

    // TODO: implement capitalization
    private String processName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("First name or last name cannot be null");
        }

        name = name.replace(" ", "");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("First name or last name cannot be empty");
        }

        return name;
    }


    private String processPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }

        phoneNumber = phoneNumber.replaceAll("[\\s\\-()]", "");

        // check if all characters are digits
        if (!phoneNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must only contain digits");
        }

        // check phone number length
        if (phoneNumber.length() != 10) {
            throw new IllegalArgumentException("Phone number should be 10 digits long");
        }

        return phoneNumber;
    }

    // validate email format for a username
    private boolean isValidEmail(String cleanEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches();
    }

    // validate password
    private boolean isValidPassword(String cleanPassword) {
        return cleanPassword.length() >= 8;
    }

    // validate first and last name
    private boolean isValidName(String cleanName) {
        return true;
    }

    // validate phone number
    private boolean isValidPhoneNumber(String cleanPhoneNumber) {
        return cleanPhoneNumber == null || cleanPhoneNumber.length() == 10;
    }


    // add new user
    public boolean addUser(User user) {
        // process input to add to database
        String cleanedUsername = processEmail(user.getUsername());
        String cleanedPassword = processPassword(user.getPassword());
        String cleanedFirstName = processName(user.getFirstName());
        String cleanedLastName = processName(user.getLastName());
        String cleanedPhoneNumber = processPhoneNumber(user.getPhoneNumber());

        // validate input
        if (!isValidEmail(cleanedUsername)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // extra rules can be added later
        if (!isValidPassword(cleanedPassword)) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!isValidPhoneNumber(cleanedPhoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        // check if the username already exists before adding new user
        if (checkUsernameExists(cleanedUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User cleanInfoUser = new User(cleanedUsername, cleanedPassword, cleanedFirstName,
                cleanedLastName, cleanedPhoneNumber);

        return repository.addUser(cleanInfoUser);
    }

    // check username/login pair
    public boolean checkUsernameLoginPair(String username, String password) {
        String cleanedUsername = processEmail(username);
        String cleanedPassword = processPassword(password);

        // validate input
        if (!isValidEmail(cleanedUsername)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // extra rules can be added later
        if (!isValidPassword(cleanedPassword)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return repository.checkUsernameLoginPair(cleanedUsername, cleanedPassword);
    }

    // check if username exists
    public boolean checkUsernameExists(String username) {
        String cleanedEmail = processEmail(username);

        if (!isValidEmail(cleanedEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        return repository.checkUsernameExists(cleanedEmail);
    }

    // get user info by username
    public User getUserInfo(String username) {
        return repository.getUserInfo(username);
    }

    // check if two passwords are the same
    public void areTwoPasswordsSame(String password1, String password2) {
        String cleanedPassword1 = processPassword(password1);
        String cleanedPassword2 = processPassword(password2);

        // extra rules can be added later
        if (!isValidPassword(cleanedPassword1)) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!isValidPassword(cleanedPassword2)) {
            throw new IllegalArgumentException("Invalid password");
        }

        if(!password1.equals(password2)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

}
