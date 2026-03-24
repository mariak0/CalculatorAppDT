package com.example.digitaltransformationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    //declare username and password
    private EditText uname, pswd;
    //declare error
    private TextView ErrorMess;
    //declare button
    private MaterialButton buttonlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //username variable - value from user input
        uname = findViewById(R.id.editTextUser);

        //password variable - value from user input
        pswd = findViewById(R.id.editTextPassword);

        //error message if password missmatch and display what is the error
        ErrorMess = findViewById(R.id.TextError);

        //button for loggin
        buttonlog = findViewById(R.id.btloggin);
        //onClick
        buttonlog.setOnClickListener(v -> {
            //username string
            String username = uname.getText().toString().trim();
            //password string
            String password = pswd.getText().toString();

            //correct username and password
            String correctUsername = "team2";
            String correctPassword = "M7R!kB2aT$";

            //check if username is correct and display error if it is incorrect
            if (!username.equals(correctUsername)) {
                ErrorMess.setText("Incorrect username. Please enter the correct team username.");
                return;
            }

            //validate the password if it is correct
            String passwordValidationMessage = validatePasswordRequirements(password);

            //check if the password is not empty
            if (!passwordValidationMessage.isEmpty()) {
                ErrorMess.setText(passwordValidationMessage);
                return;
            }

            //correct format incorrect password
            if (!password.equals(correctPassword)) {
                ErrorMess.setText("Password format is valid, but the password is incorrect.");
                return;
            }

            //login toast
            ErrorMess.setText("");
            Toast.makeText(Login.this, "Log In was successful.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Login.this, Calculator.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });
    }

    //validate password
    private String validatePasswordRequirements(String password) {
        //variable used as counter to count uppercase letter
        int upperCount = 0;

        //variable used as counter to count lower letter
        int lowerCount = 0;

        //variable used as counter to count digits
        int digitCount = 0;

        //variable used as counter to count special characters
        int specialCount = 0;

        //check password length
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            //counters check uppercase, lower, digits and special characters
            if (Character.isUpperCase(ch)) {
                upperCount++;
            } else if (Character.isLowerCase(ch)) {
                lowerCount++;
            } else if (Character.isDigit(ch)) {
                digitCount++;
            } else if (ch == '$' || ch == '!' || ch == '&') {
                specialCount++;
            }
        }

        //error message
        StringBuilder errorMessage = new StringBuilder();

        //message for length
        if (password.length() < 8 || password.length() > 12) {
            errorMessage.append("The password must have length 8-12 characters.\n");
        }

        //message for upercase
        if (upperCount < 3) {
            errorMessage.append("The password does not contain at least 3 upper-case letters.\n");
        }

        //message for lowercase
        if (lowerCount < 2) {
            errorMessage.append("The password does not contain at least 2 lower-case letters.\n");
        }

        //message for digits
        if (digitCount < 2) {
            errorMessage.append("The password does not contain at least 2 numbers.\n");
        }

        //message for special chars
        if (specialCount < 1) {
            errorMessage.append("The password does not contain at least 1 special character from: $, !, &.\n");
        }

        return errorMessage.toString().trim();
    }
}