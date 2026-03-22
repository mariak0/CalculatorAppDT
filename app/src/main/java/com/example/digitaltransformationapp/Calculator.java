package com.example.digitaltransformationapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Calculator extends AppCompatActivity {

    private TextView tvDisplay;

    private Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private Button bAdd, bSub, bMul, bDiv, bEqual, bDelete, bCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_layout);

        String username = getIntent().getStringExtra("username");
        if (username == null || username.trim().isEmpty()) {
            username = "user";
        }

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Hello, " + username + "!");

        tvDisplay = findViewById(R.id.tvDisplay);

        bCE = findViewById(R.id.bCE);

        b0 = findViewById(R.id.b0);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        b7 = findViewById(R.id.b7);
        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);

        bAdd = findViewById(R.id.bAdd);
        bSub = findViewById(R.id.bSub);
        bMul = findViewById(R.id.bMul);
        bDiv = findViewById(R.id.bDiv);
        bEqual = findViewById(R.id.bEqual);
        bDelete = findViewById(R.id.bDelete);

        b0.setOnClickListener(v -> appendValue("0"));
        b1.setOnClickListener(v -> appendValue("1"));
        b2.setOnClickListener(v -> appendValue("2"));
        b3.setOnClickListener(v -> appendValue("3"));
        b4.setOnClickListener(v -> appendValue("4"));
        b5.setOnClickListener(v -> appendValue("5"));
        b6.setOnClickListener(v -> appendValue("6"));
        b7.setOnClickListener(v -> appendValue("7"));
        b8.setOnClickListener(v -> appendValue("8"));
        b9.setOnClickListener(v -> appendValue("9"));

        bAdd.setOnClickListener(v -> appendOperator("+"));
        bSub.setOnClickListener(v -> appendOperator("-"));
        bMul.setOnClickListener(v -> appendOperator("*"));
        bDiv.setOnClickListener(v -> appendOperator("/"));

        bDelete.setOnClickListener(v -> deleteLastCharacter());
        bEqual.setOnClickListener(v -> calculateExpression());
        bCE.setOnClickListener(v -> tvDisplay.setText(""));
    }

    private void appendValue(String value) {
        String currentText = tvDisplay.getText().toString();

        if (currentText.contains("=")) {
            currentText = "";
        }

        tvDisplay.setText(currentText + value);
    }

    private void appendOperator(String operator) {
        String currentText = tvDisplay.getText().toString();

        if (currentText.isEmpty()) {
            return;
        }

        if (currentText.contains("=")) {
            String[] parts = currentText.split("=");
            currentText = parts[parts.length - 1];
        }

        char lastChar = currentText.charAt(currentText.length() - 1);
        if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
            return;
        }

        tvDisplay.setText(currentText + operator);
    }

    private void deleteLastCharacter() {
        String currentText = tvDisplay.getText().toString();

        if (!currentText.isEmpty()) {
            tvDisplay.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private void calculateExpression() {
        String expression = tvDisplay.getText().toString();

        if (expression.isEmpty()) {
            return;
        }

        if (expression.contains("=")) {
            return;
        }

        char lastChar = expression.charAt(expression.length() - 1);
        if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
            Toast.makeText(this, "Incomplete expression.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double result = evaluateWithPriority(expression);
            tvDisplay.setText(expression + "=" + formatResult(result));
        } catch (ArithmeticException e) {
            Toast.makeText(this, "Division by zero is not allowed.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Invalid expression.", Toast.LENGTH_SHORT).show();
        }
    }

    private double evaluateWithPriority(String expression) {
        ArrayList<Double> numbers = new ArrayList<>();
        ArrayList<Character> operators = new ArrayList<>();

        String[] splitNumbers = expression.split("[+\\-*/]");
        for (String num : splitNumbers) {
            if (!num.isEmpty()) {
                numbers.add(Double.parseDouble(num));
            }
        }

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                operators.add(ch);
            }
        }

        for (int i = 0; i < operators.size();) {
            char op = operators.get(i);

            if (op == '*' || op == '/') {
                double first = numbers.get(i);
                double second = numbers.get(i + 1);
                double partialResult;

                if (op == '*') {
                    partialResult = first * second;
                } else {
                    if (second == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    partialResult = first / second;
                }

                numbers.set(i, partialResult);
                numbers.remove(i + 1);
                operators.remove(i);
            } else {
                i++;
            }
        }

        double result = numbers.get(0);

        for (int i = 0; i < operators.size(); i++) {
            char op = operators.get(i);
            double next = numbers.get(i + 1);

            if (op == '+') {
                result += next;
            } else if (op == '-') {
                result -= next;
            }
        }

        return result;
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        }
        return String.valueOf(result);
    }
}