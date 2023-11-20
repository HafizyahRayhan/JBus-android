package com.HafizyahRayhanZulikhramJBusBR.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView balanceTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        balanceTextView = findViewById(R.id.balance);

        usernameTextView.setText("Hafizyah Rayhan");
        emailTextView.setText("hafizyah@ui.ac.id");
        balanceTextView.setText("$1000");
    }
}